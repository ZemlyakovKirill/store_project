package ru.themlyakov.storeproject.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.themlyakov.storeproject.entity.Attribute;
import ru.themlyakov.storeproject.entity.Product;
import ru.themlyakov.storeproject.service.KeyService;
import ru.themlyakov.storeproject.service.ValueService;
import ru.themlyakov.storeproject.util.DataManipulationException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

@Repository
public class AttributeRepository {
    private static final Logger logger = LoggerFactory.getLogger(Attribute.class);

    @Autowired
    private ValueService valueService;

    @Autowired
    private KeyService keyService;

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private NamedParameterJdbcTemplate namedTemplate;

    private static final String SELECT_ALL_BY_PRODUCT_WITH_RECURSIVE_BLOCK = """ 
            WITH RECURSIVE subattr AS (
                SELECT id, key_id, value_id, parent_id, product_id, 0 as level
                FROM attribute
                WHERE parent_id IS NULL
                UNION ALL
                SELECT a.id, a.key_id, a.value_id,a.parent_id, sa.product_id,level+1
                        FROM attribute a
                        INNER JOIN subattr sa ON sa.id = a.parent_id
            )
            """;

    private static final String SELECT_ALL_WITH_RECURSIVE_BLOCK = """
            WITH RECURSIVE subattr AS (
            SELECT id, key_id, value_id, parent_id, product_id, 0 as level
            FROM attribute
            WHERE parent_id IS NULL
            UNION ALL
            SELECT a.id, a.key_id, a.value_id,a.parent_id, sa.product_id,level+1
            FROM attribute a
            INNER JOIN subattr sa ON sa.id = a.parent_id
            )
            """;

    private static final String SELECT_BY_ID_WITH_RECURSIVE_BLOCK = """
            WITH RECURSIVE subattr AS (
            SELECT id, key_id, value_id, parent_id, product_id, 0 as level
            FROM attribute
            WHERE id= ?
            UNION ALL
            SELECT a.id, a.key_id, a.value_id,a.parent_id, sa.product_id,level+1
            FROM attribute a
            INNER JOIN subattr sa ON sa.id = a.parent_id
            )""";


    private static final String DELETE_BY_ID = """
            DELETE FROM attribute WHERE id = ?;
            """;
    private static final String SELECT_ALL_BY_PRODUCT_FROM_RECURSIVE = """
            SELECT subattr.id, key.name as key,value.name as value,parent_id,subattr.product_id,level FROM subattr\s
            JOIN key ON subattr.key_id = key.id
            JOIN value ON subattr.value_id = value.id
            where product_id = ? ORDER BY level;""";

    private static final String SELECT_ALL_FROM_RECURSIVE = """
            SELECT subattr.id, key.name as key,value.name as value,parent_id,subattr.product_id,level FROM subattr\s
            JOIN key ON subattr.key_id = key.id
            JOIN value ON subattr.value_id = value.id
            ORDER BY level;""";

    private static final String INSERT_QUERY = """
            INSERT INTO attribute(key_id,value_id,product_id,parent_id)
            VALUES (?,?,?,?) RETURNING id;
            """;

    private static final String SELECT_ALL_FROM_RECURSIVE_IN = """ 
            SELECT subattr.id, key.name as key,value.name as value,parent_id,subattr.product_id,level FROM subattr\s
            JOIN key ON subattr.key_id = key.id
            JOIN value ON subattr.value_id = value.id
            WHERE product_id IN(:products)
            ORDER BY level
            """;

    private static final String SELECT_ALL_BY_PRODUCT_ID = SELECT_ALL_BY_PRODUCT_WITH_RECURSIVE_BLOCK + SELECT_ALL_BY_PRODUCT_FROM_RECURSIVE;
    private static final String SELECT_BY_ATTRIBUTE_ID = SELECT_BY_ID_WITH_RECURSIVE_BLOCK + SELECT_ALL_FROM_RECURSIVE;

    private static final RowMapper<Attribute> ATTRIBUTE_ROW_MAPPER = (rs, rowNum) -> {
        Attribute attribute = new Attribute();
        attribute.setId(rs.getLong("id"));
        attribute.setKey(rs.getString("key"));
        attribute.setValue(rs.getString("value"));
        attribute.setLevel(rs.getInt("level"));
        attribute.setParentId(rs.getLong("parent_id"));
        attribute.setProductId(rs.getInt("product_id"));
        return attribute;
    };


    public Attribute selectAllByProductId(Long productId) {
        try {
            List<Attribute> attributeList = template.query(SELECT_ALL_BY_PRODUCT_ID, ATTRIBUTE_ROW_MAPPER, productId);
            Attribute root = attributeList.stream()
                    .filter(attribute -> attribute.getParentId() == 0 && attribute.getLevel() == 0).findFirst()
                    .orElseThrow(() -> new DataManipulationException("Attributes not found"));
            fillChildren(root, attributeList);
            return root;
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows found");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select attributes");
        }
    }

    public List<Attribute> selectAllAttributes(List<Product> products) {
        try {
            if(products == null || products.isEmpty()){
                List<Attribute> attributeList = template.query(SELECT_ALL_WITH_RECURSIVE_BLOCK+SELECT_ALL_FROM_RECURSIVE, ATTRIBUTE_ROW_MAPPER);
                List<Attribute> roots = attributeList.stream().filter(attribute -> attribute.getParentId() == 0 && attribute.getLevel() == 0).collect(Collectors.toList());
                roots.forEach(r -> fillChildren(r, attributeList));
                return roots;
            }else{
                Set<Integer> ids = products.stream().map(Product::getId).collect(Collectors.toSet());
                MapSqlParameterSource prods = new MapSqlParameterSource("products",ids);
                List<Attribute> attributeList = namedTemplate.query(SELECT_ALL_WITH_RECURSIVE_BLOCK + SELECT_ALL_FROM_RECURSIVE_IN, prods,ATTRIBUTE_ROW_MAPPER);
                List<Attribute> roots = attributeList.stream().filter(attribute -> attribute.getParentId() == 0 && attribute.getLevel() == 0).collect(Collectors.toList());
                roots.forEach(r -> fillChildren(r, attributeList));
                return roots;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows found");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select attributes");
        }
    }

    public Attribute selectById(Long attributeId) {
        try {
            List<Attribute> attributeList = template.query(SELECT_BY_ATTRIBUTE_ID, ATTRIBUTE_ROW_MAPPER, attributeId);
            Attribute root = attributeList.stream()
                    .filter(attribute -> attribute.getId().equals(attributeId)).findFirst()
                    .orElseThrow(() -> new DataManipulationException("Attributes not found"));
            fillChildren(root, attributeList);
            return root;
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows found");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select attributes");
        }
    }

    private void fillChildren(Attribute attribute, List<Attribute> attributeList) {
        if (attributeList == null || attributeList.isEmpty()) return;
        attributeList.removeIf(a -> Objects.equals(a.getId(), attribute.getId()));
        Set<Attribute> subAttributes = attributeList.stream().filter(a -> Objects.equals(a.getParentId(), attribute.getId())).collect(Collectors.toSet());
        attribute.getSubAttribute().addAll(subAttributes);
        if (!attributeList.isEmpty()) fillChildren(attributeList.getFirst(), attributeList);
    }

    @Transactional
    public Attribute insert(Attribute attribute, Long productId) {
        return recursiveInsert(attribute, productId);
    }

    private Attribute recursiveInsert(Attribute attribute, Long productId) {
        if (attribute.getParentId() == null ^ productId == null) {
            Long id = template.queryForObject(INSERT_QUERY, Long.class,
                    keyService.get(attribute.getKey()), valueService.get(attribute.getValue()),
                    productId, attribute.getParentId());

            attribute.setId(id);
            attribute.getSubAttribute().forEach(a -> {
                a.setParentId(id);
                recursiveInsert(a, null);
            });
            return attribute;
        } else throw new DataManipulationException("Product and parent id cannot be equal");
    }

    @Transactional
    public Attribute update(Attribute attribute, Long productId) {
        Integer rowsDeleted = delete(attribute.getId());
        if (rowsDeleted > 0) {
            return insert(attribute, productId);
        }
        throw new DataManipulationException("Incorrect attribute id");
    }

    public Integer delete(Long id) {
        try {
            return template.update(DELETE_BY_ID, id);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to delete attributes");
        }
    }
}
