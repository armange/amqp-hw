package com.br.amqphw.mqcommon.mq;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import com.br.amqphw.mqcommon.exception.MessageQueueException;
import com.br.amqphw.mqcommon.message.QueueMessages;
import com.rabbitmq.client.GetResponse;

@ApplicationScope
@Component
public class ReceiverMQ extends MiddlewareMQ {
    private final Logger logger = LoggerFactory.getLogger(ReceiverMQ.class);
    
    @Autowired
    public ReceiverMQ(
            @Value("${br.com.amqp.hw.middleware.host}") final String host,
            @Value("${br.com.amqp.hw.default.middleware.host}") final String defaultHost) {
        super(host, defaultHost);
    }

    public String receiveQueue(final String queueName) {
        return applyToChannel(channel -> {
            try {
                channel.queueDeclare(queueName, false, false, false, null);
                
                logger.debug(QueueMessages.WAITING_FOR_MESSAGES_ON.format(host));
                
                final GetResponse basicGet = channel.basicGet(queueName, true);
                final String message = new String(Optional
                        .ofNullable(basicGet)
                        .map(GetResponse::getBody)
                        .orElseThrow(noMoreMessages()), ENCODE_UTF8);
                
                logger.debug(QueueMessages.MESSAGE_RECEIVED.format(message));
                
                return message;
            } catch (final IOException e) {
                logger.error(e.getMessage(), e);
                
                throw new MessageQueueException(QueueMessages.ERROR_ON_RECEIVING_MESSAGE_FROM_QUEUE.format(queueName), e);
            }
        });
    }

    private Supplier<? extends MessageQueueException> noMoreMessages() {
        return () -> {
            return new MessageQueueException(QueueMessages.THERE_ARE_NO_MORE_MESSAGES.getMessage());
        };
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
