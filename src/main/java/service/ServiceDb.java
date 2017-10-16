package service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServiceDb {

      public static final void clearDatabase(JdbcTemplate jdbcTemplate) {
        final String sql = "TRUNCATE TABLE FUser";
        jdbcTemplate.update(sql);
    }
}
