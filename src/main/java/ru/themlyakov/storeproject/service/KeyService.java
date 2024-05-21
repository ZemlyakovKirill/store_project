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
public class KeyService {
    private static final Logger logger = LoggerFactory.getLogger(KeyService.class);

    private static final String INSERT_QUERY = """
                INSERT INTO key(name) VALUES(?) RETURNING id;
            """;

    private static final String SELECT_QUERY = """
                SELECT id FROM key WHERE name LIKE ?;
            """;
    private static final RowMapper<Long> KEY_ROW_MAPPER = (rs, rn) -> rs.getLong("id");

    @Autowired
    JdbcTemplate template;

    public Long get(String key) {
        try {
            if (key == null || key.length() >= 100) {
                throw new DataManipulationException("Key size must be lower than 100");
            }
            return template.queryForObject(SELECT_QUERY, Long.class, key);
        } catch (EmptyResultDataAccessException e) {
            return template.queryForObject(INSERT_QUERY, KEY_ROW_MAPPER, key);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new DataManipulationException("Unable to select key");
        }
    }


}

