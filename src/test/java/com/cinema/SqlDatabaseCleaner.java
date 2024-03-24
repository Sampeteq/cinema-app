package com.cinema;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SqlDatabaseCleaner {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public void clean() {
        var tables = jdbcTemplate.queryForList(
                "SELECT table_name " +
                        "FROM information_schema.tables " +
                        "WHERE table_schema='public'", String.class
        )
                .stream()
                .map(tableName -> "\"" + tableName + "\"")
                .collect(Collectors.joining(", "));
        jdbcTemplate.execute(String.format("TRUNCATE TABLE %s RESTART IDENTITY CASCADE", tables));
    }
}
