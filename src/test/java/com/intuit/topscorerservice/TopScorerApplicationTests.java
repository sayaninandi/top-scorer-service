package com.intuit.topscorerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.topscorerservice.dao.repository.PlayerScoreRepository;
import com.intuit.topscorerservice.data.CreateDbSchema;
import com.intuit.topscorerservice.dto.PlayerScoreResponse;
import com.intuit.topscorerservice.service.PlayerScoreService;
import com.intuit.topscorerservice.service.impl.CacheHandler;
import com.intuit.topscorerservice.service.impl.FileObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration-test")
class TopScorerApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CacheHandler cacheHandler;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PlayerScoreService playerScoreService;

    @Autowired
    private PlayerScoreRepository playerScoreRepository;

    @Autowired
    private FileObserver fileObserver;

    private static HttpHeaders headers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static CreateDbSchema dbSchema = new CreateDbSchema();

    static MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0.26")
            .withUsername("root")
            .withPassword("password")
            .withDatabaseName("craft_demo");

    private static GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:6.2.6")
            .withAccessToHost(true)
            .withExposedPorts(6379);

    static {
        REDIS_CONTAINER.start();
        MYSQL_CONTAINER.start();
    }


    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);

        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort);
    }

    @BeforeAll
    public static void init() throws IOException {
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        dbSchema.prepareSchema(MYSQL_CONTAINER);
    }

    private String createURLWithPort() {
        return STR."http://localhost:\{port}/v1/scores";
    }

    @Test
    public void test_ShouldReturnEmptyOnEmptyTable() throws JsonProcessingException {

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ParameterizedTypeReference<List<PlayerScoreResponse>> responseType = new ParameterizedTypeReference<List<PlayerScoreResponse>>() {};
        ResponseEntity<List<PlayerScoreResponse>> response = restTemplate.exchange(
                (STR."\{createURLWithPort()}/top"),
                HttpMethod.GET,
                entity,
                responseType
        );

        List<PlayerScoreResponse> responseBody = response.getBody();
        String expected = "[]";

        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals(expected, objectMapper.writeValueAsString(responseBody));
        assertNotNull(responseBody);
    }
}
