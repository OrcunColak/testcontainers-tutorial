package oracle.xe;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.OracleContainer;
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
class XeTest {

    private static final String TABLE_NAME = "all_char_types_table";

    @Container
    public OracleContainer container = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withInitScript("oracle/xe/oracle-xe-init.sql")
            .withLogConsumer(outputFrame -> {
                // Handle container output as needed
                // For example, you can log or process the output here
            });

    @Test
    void selectTest() {
        String jdbcUrl = container.getJdbcUrl();
        String username = container.getUsername();
        String password = container.getPassword();

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
