package neo4j;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
@Slf4j
class Neo4JTest {

    @SuppressWarnings("resource")
    @Container
    private static final Neo4jContainer<?> NEO_4_J_CONTAINER = new Neo4jContainer<>(DockerImageName.parse("neo4j:5"))
            // .withRandomPassword()
            .withoutAuthentication()
            .withStartupTimeout(Duration.ofMinutes(2));

    @Test
    void testNeo4jContainer() {
        String neo4jUri = NEO_4_J_CONTAINER.getBoltUrl();
        String neo4jPassword = NEO_4_J_CONTAINER.getAdminPassword();

        assertNotNull(neo4jUri);
        assertNull(neo4jPassword);

        try (Driver driver = GraphDatabase.driver(NEO_4_J_CONTAINER.getBoltUrl(), AuthTokens.none());
             Session session = driver.session()) {
        }

    }

}
