package com.br.amqphw;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

import com.br.amqphw.mqcommon.mq.ReceiverMQ;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class ServiceOneApplicationTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverMQ.class);
	
    private static final String RABBITMQ_CONTAINER_NAME = "rabbitmq:3-management";
	private static final String _15672_15672_PORT_BINDING = "15672:15672";
	private static final String _5672_5672_PORT_BINDING = "5672:5672";
	private static final String SERVER_STARTUP_COMPLETE_REGEX = ".* Server startup complete.*";
	private static final String SLASH = "/";
    private static final String HTTP_LOCALHOST = "http://localhost:";
    private static final String QUEUE_NAME = "test_queue";
    private static final String QUEUE_MESSAGE = "Hello Test";
    private static final String SEND_URI_KEY = "send";
    private static final String RECEIVE_URI_KEY = "receive";
    private static final String BASE_PATH_API = "/amqp/";
    
    private static volatile boolean RABBITMQ_STOPED = true;
    private static volatile boolean RABBITMQ_STARTED = false;
    private static volatile boolean MESSAGE_SENT = false;
    
    @Container
    public GenericContainer<?> rabbitmq = new GenericContainer<>(RABBITMQ_CONTAINER_NAME);
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Value("${spring.security.user.name:user}")
    private String user;
    
    @Value("${spring.security.user.password}") 
    private String password;
    
    @BeforeEach
    public void beforeEach() throws InterruptedException {
    	startRabbitMQIfNotRunning(rabbitmq);
    }

	private static synchronized void startRabbitMQIfNotRunning(final GenericContainer<?> rabbitmq) {
		if (RABBITMQ_STOPED) {
			RABBITMQ_STOPED = false;
			
			configureRabbitMQ(rabbitmq);
			
			rabbitmq.start();
			
			RABBITMQ_STARTED = true;
		}
	}
    
    public void afterAll() {
    	rabbitmq.stop();
    }

	private static void configureRabbitMQ(final GenericContainer<?> rabbitmq) {
		final List<String> portBindings = new ArrayList<>(2);
		
		portBindings.add(_5672_5672_PORT_BINDING);
		portBindings.add(_15672_15672_PORT_BINDING);
		
		final List<Consumer<OutputFrame>> logConsumers = new ArrayList<>(1);
    	
    	logConsumers.add(opf -> LOGGER.info(opf.getUtf8String()));
    	
    	rabbitmq.setPortBindings(portBindings);
    	rabbitmq.waitingFor(Wait.forLogMessage(SERVER_STARTUP_COMPLETE_REGEX, 1));
		rabbitmq.setLogConsumers(logConsumers);
	}
    
    @Test
    @Order(1)
    public void sendAMessage() throws Exception {
    	if (!RABBITMQ_STARTED) {
    		Thread.sleep(1000);
    	}
    	
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
        
        Thread.sleep(5000);

        assertThat(response).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        
        MESSAGE_SENT = true;
    }

    @Test
    @Order(2)
    public void receiveAMessage() throws Exception {
    	if (!RABBITMQ_STARTED || !MESSAGE_SENT) {
    		Thread.sleep(1000);
    	}
    	
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
