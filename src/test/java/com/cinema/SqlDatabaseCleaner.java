package com.cinema;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqlDatabaseCleaner {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public void clean() {
        jdbcTemplate.queryForList(
                "SELECT table_name " +
                        "FROM information_schema.tables " +
                        "WHERE table_schema='public'", String.class
        ).forEach(tableName -> jdbcTemplate.execute(
                "TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE"
        ));
    }
}
