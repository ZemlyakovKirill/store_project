package ru.themlyakov.storeproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.themlyakov.storeproject.util.DataManipulationException;

@Service
public class ValueService {
    private static final Logger logger = LoggerFactory.getLogger(ValueService.class);

    private static final String INSERT_QUERY = """
                INSERT INTO value(name) VALUES(?) RETURNING id;
            """;

    private static final String SELECT_QUERY = """
                SELECT id FROM value WHERE name LIKE ?;
            """;
    private static final RowMapper<Long> VALUE_ROW_MAPPER = (rs, rn) -> rs.getLong("id");

    @Autowired
    JdbcTemplate template;

    public Long get(String value) {
        try {

            return value != null ? template.queryForObject(SELECT_QUERY, Long.class, value) : 1L;
        } catch (EmptyResultDataAccessException e) {
            return template.queryForObject(INSERT_QUERY, VALUE_ROW_MAPPER, value);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select key");
        }
    }


}
