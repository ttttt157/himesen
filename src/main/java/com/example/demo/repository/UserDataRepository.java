// UserDataRepository.java
package com.example.demo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class UserDataRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserDataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getUserData(String username) {
        String sql = "SELECT * FROM userdata WHERE username=?";
        return jdbcTemplate.queryForMap(sql, username);
    }

    public void updateResources(String username, int food, int fund, int material) {
        String sql = "UPDATE userdata SET havefood=?, havefund=?, havematerial=? WHERE username=?";
        jdbcTemplate.update(sql, food, fund, material, username);
    }
}
