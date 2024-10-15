package postgres;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


@Testcontainers
@Slf4j
class PostgresWithCopyFileToContainerTest {

    @SuppressWarnings("resource")
    @Container
    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withCopyFileToContainer(MountableFile.forClasspathResource("postgres/postgres-create-extension.sql"), "/docker-entrypoint-initdb.d/init.sql");


    @Test
    void selectTest() {
        // jdbc:postgresql://localhost:15313/test?loggerLevel=OFF
        String jdbcUrl = CONTAINER.getJdbcUrl();
        // test
        String username = CONTAINER.getUsername();
        // test
        String password = CONTAINER.getPassword();

        log.info("Connection jdbcUrl: {} , username: {} , password : {}", jdbcUrl, username, password);
        String selectQuery = "SELECT uuid_generate_v4() AS uuid";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = connection.prepareStatement(selectQuery);
             ResultSet resultSet = stmt.executeQuery()) {

            log.info("Connected to database");
            if (resultSet.next()) {
                String generatedUuid = resultSet.getString("uuid");
                log.info("Generated UUID v4: {}", generatedUuid);

                // Validate that the UUID is not null and has the expected format
                assertNotNull(generatedUuid, "The generated UUID should not be null");
            }
        } catch (Exception exception) {
            log.error("Exception ", exception);
            fail("Test failed with exception " + exception.getMessage());
        }
    }
}