package network;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * As far as I see even if the network is not closed, testcontainers still deletes it from docker
 */
@Testcontainers
@Slf4j
class NetworkNotClosedTest {

    // Define a shared network for the containers
    private static final Network network = Network.newNetwork();

    @SuppressWarnings("resource")
    @Container
    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:11.19-bullseye")
            .withNetwork(network)
            .withNetworkAliases("postgres-db");

    @Test
    void testDatabaseConnectionWithoutClosingNetwork() throws SQLException {
        // Get JDBC URL, username, and password from the PostgreSQL container
        String jdbcUrl = CONTAINER.getJdbcUrl();
        String username = CONTAINER.getUsername();
        String password = CONTAINER.getPassword();

        // Make a database connection
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Create a simple table
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE test_table (id SERIAL PRIMARY KEY, name VARCHAR(255))")) {
                statement.execute();
                log.info("Created table successfully");
            }

            // Insert a record
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO test_table (name) VALUES (?)")) {
                statement.setString(1, "Test Name");
                statement.execute();
                log.info("Inserted into table successfully");
            }

            // Query the database
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM test_table")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Verify the result
                    if (resultSet.next()) {
                        log.info("Selected from table successfully");
                        assertEquals("Test Name", resultSet.getString("name"), "Unexpected value in the database");
                    } else {
                        throw new AssertionError("No records found in the database");
                    }
                }
            }
        }
    }
}
