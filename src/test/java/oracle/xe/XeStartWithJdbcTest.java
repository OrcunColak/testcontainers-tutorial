package oracle.xe;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
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
class XeStartWithJdbcTest {

    private static final String JDBC_URL = "jdbc:tc:postgresql:9.6.8:///testdb";
    private static final String USER = "test";
    private static final String PASSWORD = "test";

    @Test
    void selectTest() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            log.info("Connected to database");
            String query = "SELECT 1 AS column1, 'Hello' AS column2;";
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
