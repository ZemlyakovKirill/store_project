package ru.themlyakov.storeproject.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.themlyakov.storeproject.entity.Supplier;
import ru.themlyakov.storeproject.util.DataManipulationException;

import java.util.List;

@Repository
public class SupplierRepository implements SimpleRepository<Supplier, Long> {

    private static final Logger logger = LoggerFactory.getLogger(Supplier.class);
    private static final String INSERT_SUPPLIER_QUERY = "INSERT INTO supplier(name,address,contact_info) VALUES(?,?,?) RETURNING *;";

    private static final String UPDATE_SUPPLIER_QUERY = "UPDATE supplier SET name=?,address=?,contact_info=? WHERE id=? RETURNING *;";

    private static final String DELETE_SUPPLIER_QUERY = "DELETE FROM supplier WHERE id = ? RETURNING *;";

    private static final String SELECT_ALL_SUPPLIER_QUERY = "SELECT * FROM supplier;";

    private static final String SELECT_SUPPLIER_QUERY = "SELECT * FROM supplier WHERE id = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final RowMapper<Supplier> SUPPLIER_ROW_MAPPER = (rs, rowNum) -> {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getLong("id"));
        supplier.setName(rs.getString("name"));
        supplier.setAddress(rs.getString("address"));
        supplier.setContactInfo(rs.getString("contact_info"));
        return supplier;
    };

    @Override
    public Supplier insert(Supplier supplier) {
        try {
            return jdbcTemplate.queryForObject(INSERT_SUPPLIER_QUERY, SUPPLIER_ROW_MAPPER
                    , supplier.getName(), supplier.getAddress(), supplier.getContactInfo());
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows inserted");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to insert supplier");
        }
    }

    @Override
    public Supplier update(Supplier supplier) {
        try {
            return jdbcTemplate.queryForObject(UPDATE_SUPPLIER_QUERY, SUPPLIER_ROW_MAPPER
                    , supplier.getName(), supplier.getAddress(), supplier.getContactInfo(), supplier.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows updated");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to update supplier");
        }
    }

    @Override
    public Supplier delete(Long id) {
        try {
            return jdbcTemplate.queryForObject(DELETE_SUPPLIER_QUERY, SUPPLIER_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows deleted");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to delete supplier");
        }
    }

    @Override
    public List<Supplier> selectAll() {
        try {
            return jdbcTemplate.query(SELECT_ALL_SUPPLIER_QUERY, SUPPLIER_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Empty rows");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select supplier");
        }
    }

    @Override
    public Supplier selectById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_SUPPLIER_QUERY, SUPPLIER_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Empty rows");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select supplier");
        }
    }
}
