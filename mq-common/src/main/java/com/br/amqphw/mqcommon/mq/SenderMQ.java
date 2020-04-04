package com.br.amqphw.mqcommon.mq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import com.br.amqphw.mqcommon.message.QueueMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.armange.commons.thread.ThreadBuilder;

@ApplicationScope
@Component
public class SenderMQ extends MiddlewareMQ {
    private static final String QUEUE_NAME = "hello";
    private static final String DEFAULT_MESSAGE = "Hello world.";
    private static final String EXCHANGE = "";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ObjectMapper objectMapper;
    
    private volatile boolean counting;
    
    @Autowired
    public SenderMQ(
            @Value("${br.com.amqp.hw.middleware.host}") final String host,
            @Value("${br.com.amqp.hw.default.middleware.host}") final String defaultHost,
            final ObjectMapper objectMapper) {
        super(host, defaultHost);
        
        this.objectMapper = objectMapper;
    }
    
    public void stopThreadMessageSending() {
        counting = false;
    }
    
    public void startThreadMessageSendingEachFiveSeconds() {
        ThreadBuilder
            .newBuilder()
            .setInterval(1000 * 5)
            .setUncaughtExceptionConsumer(throwable -> logger.error(throwable.getMessage(), throwable))
            .setExecution(() -> startMessageSendingEachFiveSeconds());
    }
    
    private void startMessageSendingEachFiveSeconds() {
        consumeChannel(channel -> {
            try {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                
                int counter = 0;
                
                counting = true;
                
                while(counting) {
                    counter++;
                    
                    final String formattedMessage = String.format(DEFAULT_MESSAGE, counter);
                    
                    channel.basicPublish(EXCHANGE, QUEUE_NAME, null, formattedMessage.getBytes());
                    
                    logger.debug(QueueMessages.MESSAGE_SENT.format(formattedMessage));
                    
                    Thread.sleep(1000 * 5);
                }
            } catch(final IOException | InterruptedException e) {
                logger.error(e.getMessage(), e);
                
                throw new RuntimeException(e);
            }
        });
    }
    
    public void sendMessage(final QueueMessage queueMessage) {
        validateQueueMessage(queueMessage);
        
        consumeChannel(channel -> {
            try {
                channel.queueDeclare(queueMessage.getQueueName(), false, false, false, null);
                channel.basicPublish(EXCHANGE, queueMessage.getQueueName(), null, serialize(queueMessage).getBytes());
                
                logger.debug(QueueMessages.MESSAGE_SENT.format(queueMessage.getMessage()));
            } catch (final IOException e) {
                logger.error(e.getMessage(), e);
                
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }
    
    private String serialize(final QueueMessage queueMessage) {
        try {
            return objectMapper.writeValueAsString(queueMessage);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
