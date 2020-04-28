package com.br.amqphw;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("mock-message-queue")
public class ServiceOneApplicationTest {
    private static final String SLASH = "/";
    private static final String HTTP_LOCALHOST = "http://localhost:";
    private static final String QUEUE_NAME = "test_queue";
    private static final String QUEUE_MESSAGE = "Hello Test";
    private static final String SEND_URI_KEY = "send";
    private static final String RECEIVE_URI_KEY = "receive";
    private static final String BASE_PATH_API = "/amqp/";
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Value("${spring.security.user.name:user}")
    private String user;
    
    @Value("${spring.security.user.password}") 
    private String password;

    @Test
    public void sendAMessage() throws Exception {
        final String uri = new StringBuilder(HTTP_LOCALHOST)
                .append(port)
                .append(BASE_PATH_API)
                .append(QUEUE_NAME)
                .append(SLASH)
                .append(SEND_URI_KEY)
                .toString();

        final ResponseEntity<Void> response = restTemplate
                .withBasicAuth(user, password)
                .postForEntity(uri, QUEUE_MESSAGE, Void.class);

        assertThat(response).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    public void receiveAMessage() throws Exception {
        final String uri = new StringBuilder(HTTP_LOCALHOST)
                .append(port)
                .append(BASE_PATH_API)
                .append(QUEUE_NAME)
                .append(SLASH)
                .append(RECEIVE_URI_KEY)
                .toString();

        final ResponseEntity<String> response = restTemplate
                .withBasicAuth(user, password)
                .getForEntity(uri, String.class);

        assertThat(response).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
