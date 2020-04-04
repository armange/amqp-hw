package com.br.amqphw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.server.ResponseStatusException;

import com.br.amqphw.mqcommon.exception.MessageQueueException;
import com.br.amqphw.mqcommon.mq.ReceiverMQ;

@ApplicationScope
@Component
public class ReceiverService extends AbstractMQService {
    private final ReceiverMQ receiverMQ;
    
    @Autowired
    public ReceiverService(final ReceiverMQ receiverMQ) {
        this.receiverMQ = receiverMQ;
    }

    public String receiveMessage(final String queueName) {
        try {
            return receiverMQ.receiveQueue(queueName);
        } catch (final MessageQueueException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (final Exception e) {
            throw classifyException(e);
        }
    }
}
