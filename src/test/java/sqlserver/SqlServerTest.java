package sqlserver;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MSSQLServerContainer;
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
class SqlServerTest {

    private static final String TABLE_NAME = "datetime_types_table";

    @SuppressWarnings("resource")
    @Container
    private static final MSSQLServerContainer<?> CONTAINER = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest")
            .acceptLicense()
            // See https://learn.microsoft.com/en-us/sql/connect/jdbc/using-basic-data-types?view=sql-server-ver16
            // "To use java.sql.Time with the time SQL Server type, you must set the sendTimeAsDatetime
            // connection property to false."
            .withUrlParam("sendTimeAsDateTime", "false")
            .withInitScript("sqlserver/sqlserver-init.sql");

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
