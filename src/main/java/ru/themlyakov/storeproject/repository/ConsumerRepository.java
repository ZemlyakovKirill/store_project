package ru.themlyakov.storeproject.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.themlyakov.storeproject.entity.Consumer;
import ru.themlyakov.storeproject.util.DataManipulationException;

import java.util.List;

@Repository
public class ConsumerRepository implements SimpleRepository<Consumer, Long> {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private static final String INSERT_CONSUMER_QUERY = "INSERT INTO consumer(first_name,last_name,middle_name,address,contact_info) VALUES(?,?,?,?,?) RETURNING *;";

    private static final String UPDATE_CONSUMER_QUERY = "UPDATE consumer SET first_name=?,last_name=?,middle_name=?,address=?,contact_info=? WHERE id=? RETURNING *;";

    private static final String DELETE_CONSUMER_QUERY = "DELETE FROM consumer WHERE id = ? RETURNING *;";

    private static final String SELECT_ALL_CONSUMER_QUERY = "SELECT * FROM consumer;";

    private static final String SELECT_CONSUMER_QUERY = "SELECT * FROM consumer WHERE id = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final RowMapper<Consumer> CONSUMER_ROW_MAPPER = (rs, rowNum) -> {
        Consumer consumer = new Consumer();
        consumer.setId(rs.getLong("id"));
        consumer.setFirstName(rs.getString("first_name"));
        consumer.setLastName(rs.getString("last_name"));
        consumer.setAddress(rs.getString("address"));
        consumer.setContactInfo("contact_info");
        return consumer;
    };

    @Override
    public Consumer insert(Consumer consumer) {
        try {
            return jdbcTemplate.queryForObject(INSERT_CONSUMER_QUERY, CONSUMER_ROW_MAPPER
                    , consumer.getFirstName(), consumer.getLastName(), consumer.getMiddleName(), consumer.getAddress(), consumer.getContactInfo());
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows inserted");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to insert consumer");
        }
    }

    @Override
    public Consumer update(Consumer consumer) {
        try {
            return jdbcTemplate.queryForObject(UPDATE_CONSUMER_QUERY, CONSUMER_ROW_MAPPER
                    , consumer.getFirstName(), consumer.getLastName(), consumer.getMiddleName(), consumer.getAddress(), consumer.getContactInfo(), consumer.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows updated");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to update consumer");
        }
    }

    @Override
    public Consumer delete(Long id) {
        try {
            return jdbcTemplate.queryForObject(DELETE_CONSUMER_QUERY, CONSUMER_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("No rows deleted");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to delete consumer");
        }
    }

    @Override
    public List<Consumer> selectAll() {
        try {
            return jdbcTemplate.query(SELECT_ALL_CONSUMER_QUERY, CONSUMER_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Empty rows");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select consumer");
        }
    }

    @Override
    public Consumer selectById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_CONSUMER_QUERY, CONSUMER_ROW_MAPPER,id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataManipulationException("Empty rows");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select consumer");
        }
    }
}
