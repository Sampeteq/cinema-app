package code.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;


@RequiredArgsConstructor
@Component
public class DbCleaner {

    private final DataSource dataSource;

    public void clean() {
        try (
                var connection = dataSource.getConnection();
                var resultSet = connection
                        .createStatement()
                        .executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public'")
        ) {
            while (resultSet.next()) {
                var tableName = resultSet.getString("table_name");
                try (
                        var truncateStatement = connection.createStatement()
                ) {
                    truncateStatement.executeUpdate("TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE");
                }
            }
        } catch (SQLException exception) {
            throw new IllegalArgumentException(exception);
        }
    }
}
