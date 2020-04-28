package com.br.amqphw.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.br.amqphw.mqcommon.mq.ReceiverMQ;
import com.br.amqphw.mqcommon.mq.SenderMQ;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Profile("mock-message-queue")
public class IntegrationTestConfig {
    public static final String MOCKED_MESSAGE = "Mocked message";

    @Value("${br.com.amqp.hw.middleware.host}")
    private String host;

    @Value("${br.com.amqp.hw.default.middleware.host}")
    private String defaultHost;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @Primary
    public SenderMQ senderMQMock() {
        final SenderMQ spy = spy(new SenderMQ(host, defaultHost, objectMapper));

        doNothing().when(spy).sendMessage(any());

        return spy;
    }

    @Bean
    @Primary
    public ReceiverMQ receiverMQMock() {
        final ReceiverMQ spy = spy(new ReceiverMQ(host, defaultHost));

        doReturn(MOCKED_MESSAGE).when(spy).receiveQueue(any());

        return spy;
    }
}
