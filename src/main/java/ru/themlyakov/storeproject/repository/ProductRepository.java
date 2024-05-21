package ru.themlyakov.storeproject.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.themlyakov.storeproject.entity.Attribute;
import ru.themlyakov.storeproject.entity.Product;
import ru.themlyakov.storeproject.util.DataManipulationException;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository implements SimpleRepository<Product, Long> {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private AttributeRepository attributeRepository;
    private static final Logger logger = LoggerFactory.getLogger(Product.class);
    private static final String INSERT_PRODUCT_QUERY = "INSERT INTO product(name,cost) VALUES(?,?) RETURNING *;";

    private static final String UPDATE_PRODUCT_QUERY = "UPDATE product SET name=?,cost=? WHERE id=? RETURNING *;";

    private static final String DELETE_PRODUCT_QUERY = "DELETE FROM product WHERE id = ? RETURNING *;";

    private static final String SELECT_ALL_PRODUCT_QUERY = """
            SELECT * FROM product;""";

    private static final String SELECT_PRODUCT_QUERY = "SELECT * FROM product WHERE id = ?;";

    private static final RowMapper<Product> PRODUCT_ROW_MAPPER = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setCost(rs.getBigDecimal("cost"));
        return product;
    };

    private static final RowMapper<Attribute> ATTRIBUTE_ROW_MAPPER = (rs, rowNum) -> {
        Attribute attribute = new Attribute();
        attribute.setId(rs.getLong("attribute_id"));
        attribute.setKey(rs.getString("key"));
        attribute.setValue(rs.getString("value"));
        attribute.setLevel(rs.getInt("level"));
        attribute.setParentId(rs.getLong("parent_id"));
        return attribute;
    };

    @Override
    public Product insert(Product product) {
        try {
            return template.queryForObject(INSERT_PRODUCT_QUERY, PRODUCT_ROW_MAPPER
                    , product.getName(), product.getCost());
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows inserted");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to insert product");
        }
    }

    @Override
    public Product update(Product product) {
        try {
            return template.queryForObject(UPDATE_PRODUCT_QUERY, PRODUCT_ROW_MAPPER
                    , product.getName(), product.getCost(), product.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows updated");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to update product");
        }
    }

    @Override
    public Product delete(Long id) {
        try {
            return template.queryForObject(DELETE_PRODUCT_QUERY, PRODUCT_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows deleted");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to delete product");
        }
    }

    @Override
    @Transactional
    public List<Product> selectAll() {
        try {
            List<Product> products = template.query(SELECT_ALL_PRODUCT_QUERY, PRODUCT_ROW_MAPPER);
            List<Attribute> attributeList = attributeRepository.selectAllAttributes(products);
            products.forEach(p ->
                    {
                        Optional<Attribute> attr = attributeList.stream().filter(a -> a.getProductId().equals(p.getId())).findFirst();
                        attr.ifPresent(p::setAttribute);
                    }
            );
            return products;
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Empty rows");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select product");
        }
    }

    @Override
    public Product selectById(Long id) {
        try {
            return template.queryForObject(SELECT_PRODUCT_QUERY, PRODUCT_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Empty rows");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select product");
        }
    }
}
