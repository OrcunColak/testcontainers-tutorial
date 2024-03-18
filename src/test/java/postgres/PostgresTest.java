package postgres;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
@Slf4j
class PostgresTest {

    private static final String TABLE_NAME = "all_char_types_table";

    @SuppressWarnings("resource")
    @Container
    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:11.19-bullseye")
            .withInitScript("postgres/postgres-init.sql");

    @Test
    void selectTest() {
        // jdbc:postgresql://localhost:15313/test?loggerLevel=OFF
        String jdbcUrl = CONTAINER.getJdbcUrl();
        // test
        String username = CONTAINER.getUsername();
        // test
        String password = CONTAINER.getPassword();

        log.info("Connection jdbcUrl: {} , username: {} , password : {}", jdbcUrl, username, password);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            log.info("Connected to database");
            String query = "SELECT * FROM " + TABLE_NAME;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                printColumns(resultSet);
            }

        } catch (Exception exception) {
            log.error("Exception ", exception);
            fail("Test failed with exception " + exception.getMessage());
        }
    }

    private static void printColumns(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        int rowCount = 1;
        // Print column values
        while (resultSet.next()) {
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                String columnName = metaData.getColumnName(columnIndex);
                Object object = resultSet.getObject(columnIndex);
                log.info("Row : {} {} : {} ", rowCount, columnName, object);
            }
        }
    }
}
