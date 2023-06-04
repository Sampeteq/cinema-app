package code.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;


@Component
@RequiredArgsConstructor
public class DbCleaner {
    private static final String TABLES_NAMES_SQL =
            "SELECT table_name " +
            "FROM information_schema.tables " +
            "WHERE table_schema='public'";
    private static final String TRUNCATE_SQL = "TRUNCATE TABLE ";
    private static final String RESTART_IDENTITY = " RESTART IDENTITY CASCADE";
    private final DataSource dataSource;

    public void clean() {
        try (
                var connection = dataSource.getConnection();
                var resultSet = connection
                        .createStatement()
                        .executeQuery(TABLES_NAMES_SQL)
        ) {
            while (resultSet.next()) {
                var tableName = resultSet.getString("table_name");
                try (var statement = connection.createStatement()) {
                    statement.executeUpdate(TRUNCATE_SQL + tableName + RESTART_IDENTITY);
                }
            }
        } catch (SQLException exception) {
            throw new IllegalArgumentException(exception);
        }
    }
}
