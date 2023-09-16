package com.cinema;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlDatabaseCleaner {
    private static final String TABLES_NAMES_SQL =
            "SELECT table_name " +
            "FROM information_schema.tables " +
            "WHERE table_schema='public'";
    private static final String TRUNCATE_SQL = "TRUNCATE TABLE ";
    private static final String RESTART_IDENTITY = " RESTART IDENTITY CASCADE";
    private final DataSource dataSource;

    public SqlDatabaseCleaner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void clean() {
        try (
                var connection = dataSource.getConnection();
                var resultSet = connection
                        .createStatement()
                        .executeQuery(TABLES_NAMES_SQL)
        ) {
            resetTables(connection, resultSet);
        } catch (SQLException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    private static void resetTables(Connection connection, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            var tableName = resultSet.getString("table_name");
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(TRUNCATE_SQL + tableName + RESTART_IDENTITY);
            }
        }
    }
}
