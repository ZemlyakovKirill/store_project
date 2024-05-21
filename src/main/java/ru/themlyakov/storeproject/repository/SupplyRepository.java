package ru.themlyakov.storeproject.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.themlyakov.storeproject.entity.Product;
import ru.themlyakov.storeproject.entity.Supplier;
import ru.themlyakov.storeproject.util.DataManipulationException;

@Repository
public class SupplyRepository {
    private static final Logger logger = LoggerFactory.getLogger(SupplyRepository.class);

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    private static final String MAKE_SUPPLY = """
            INSERT INTO supply(supplier_id,product_id,quantity) VALUES(?,?,?);
            """;
    private static final String DELETE_SUPPLY = """
                        DELETE FROM supply WHERE id = ?;
            """;

    private static final String DELETE_ALL_SUPPLIES = """
            DELETE FROM supply WHERE supplier_id=? AND product_id=?;
            """;

    public Integer makeSupply(Long supplierId, Long productId, Integer quantity) {
        Product product;
        Supplier supplier;
        try {
            product = productRepository.selectById(productId);
        } catch (DataManipulationException e) {
            throw new DataManipulationException("Product was not found");
        }
        try {
            supplier = supplierRepository.selectById(supplierId);

        } catch (DataManipulationException e) {
            throw new DataManipulationException("Supplier was not found");
        }
        return save(supplier, product, quantity);
    }

    private Integer save(Supplier supplier, Product product, Integer quantity) {
        try {
            return template.update(MAKE_SUPPLY, supplier.getId(), product.getId(), quantity);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Cannot insert supply");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Cannot insert supply");
        }
    }

    public Integer deleteAllSupplies(Long supplierId, Long productId) {
        try {
            productRepository.selectById(productId);
        } catch (DataManipulationException e) {
            throw new DataManipulationException("Product was not found");
        }
        try {
            supplierRepository.selectById(supplierId);

        } catch (DataManipulationException e) {
            throw new DataManipulationException("Supplier was not found");
        }
        try {
            return template.update(DELETE_ALL_SUPPLIES, supplierId, productId);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Cannot delete supplies");
        } catch (DataAccessException e) {
            logger.info(e.getMessage());
            throw new DataManipulationException("Cannot delete supplies");
        }

    }

    public Integer delete(Long supplyId) {
        try {
            return template.update(DELETE_SUPPLY, supplyId);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Cannot insert supply");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Cannot insert supply");
        }
    }
}
