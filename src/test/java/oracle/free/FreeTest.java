package oracle.free;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
@Slf4j
class FreeTest {

    private static final String TABLE_NAME = "all_char_types_table";

    // The LogMessageWaitStrategy for the Oracle XE container was set to 240 seconds, but with the Oracle Free Container,
    // it has been reduced to 60 seconds, which is too short.
    // We need to use a longer wait strategy.
    private static final WaitStrategy WAIT_STRATEGY = new LogMessageWaitStrategy()
            .withRegEx(".*DATABASE IS READY TO USE!.*\\s")
            .withTimes(1)
            .withStartupTimeout(Duration.of(240, ChronoUnit.SECONDS));

    @Container
    public OracleContainer container = new OracleContainer("gvenzl/oracle-free:23-slim-faststart")
            .waitingFor(WAIT_STRATEGY)
            .withCopyFileToContainer(MountableFile.forClasspathResource("oracle/free/oracle-free-create-user.sql"), "/container-entrypoint-startdb.d/init.sql")
            .withInitScript("oracle/free/oracle-free-init.sql");

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
