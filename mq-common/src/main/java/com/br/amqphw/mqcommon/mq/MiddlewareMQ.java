package com.br.amqphw.mqcommon.mq;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.br.amqphw.common.message.DefaultMessages;
import com.br.amqphw.mqcommon.exception.MessageQueueException;
import com.br.amqphw.mqcommon.message.QueueMessages;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

abstract class MiddlewareMQ {
    private static final String QUEUE_MESSAGE_PARAM_NAME = "queueMessage";
    
    protected static final String QUEUE_NAME = "hello_world";
    
    public static final String ENCODE_UTF8 = "UTF-8";
    
    protected final String host;
    
    public MiddlewareMQ(final String host, final String defaultHost) {
        this.host = Optional.ofNullable(host).orElse(defaultHost);
    }
    
    protected void consumeChannel(final Consumer<Channel> channelConsumer) {
        applyToChannel(channel -> {channelConsumer.accept(channel); return null;});
    }
    
    protected String applyToChannel(final Function<Channel, String> channelFunction) {
        final ConnectionFactory factory = new ConnectionFactory();
        
        factory.setHost(host);
        
        getLogger().debug(QueueMessages.CONNECTING_TO_DE_MIDWARE.format(host));
        
        try (
                final Connection connection = factory.newConnection();
                final Channel channel = connection.createChannel()) 
        {
            return channelFunction.apply(channel);
        } catch (final IOException | TimeoutException e) {
            getLogger().error(e.getMessage(), e);
            
            throw new RuntimeException(e);
        }
    }
    
    protected void validateQueueMessage(final QueueMessage queueMessage) {
        Objects.requireNonNull(queueMessage, DefaultMessages.THE_PARAMETER_IS_REQUIRED.format(QUEUE_MESSAGE_PARAM_NAME));
        
        if (StringUtils.isBlank(queueMessage.getQueueName())) {
            throw new MessageQueueException(QueueMessages.QUEUE_NAME_REQUIRED.getMessage());
        }
        
        if (StringUtils.isBlank(queueMessage.getMessage())) {
            throw new MessageQueueException(QueueMessages.QUEUE_MESSAGE_REQUIRED.getMessage());
        }
    }
    
    protected abstract Logger getLogger();
}
