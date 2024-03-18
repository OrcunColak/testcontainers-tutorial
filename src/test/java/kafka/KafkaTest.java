package kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Properties;

@Testcontainers
@Slf4j
class KafkaTest {

    private static final Network NETWORK = Network.newNetwork();
    @Container
    private static final KafkaContainer CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.2"))
                    .withNetwork(NETWORK);

    @SuppressWarnings("resource")
    @Container
    private static final GenericContainer<?> SCHEMA_REGISTRY =
            new GenericContainer<>(DockerImageName.parse("confluentinc/cp-schema-registry:7.5.2"))
                    .withNetwork(NETWORK)
                    .withExposedPorts(8081)
                    .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
                    .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
                    .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS",
                            "PLAINTEXT://" + CONTAINER.getNetworkAliases().get(0) + ":9092")
                    .waitingFor(Wait.forHttp("/subjects").forStatusCode(200));

    @Test
    void testConnect() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, CONTAINER.getBootstrapServers());
        properties.put("schema.registry.url", "http://" + SCHEMA_REGISTRY.getHost() +
                                                 ":" + SCHEMA_REGISTRY.getFirstMappedPort());
        // ...
    }

}
