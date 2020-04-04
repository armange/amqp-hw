package com.br.amqphw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.server.ResponseStatusException;

import com.br.amqphw.mqcommon.exception.MessageQueueException;
import com.br.amqphw.mqcommon.mq.QueueMessage;
import com.br.amqphw.mqcommon.mq.SenderMQ;

@ApplicationScope
@Component
public class SenderService extends AbstractMQService {
    private final SenderMQ senderMQ;
    
    @Autowired
    public SenderService(final SenderMQ senderMQ) {
        this.senderMQ = senderMQ;
    }

    public void sendMessage(final QueueMessage queueMessage) {
        try {
            senderMQ.sendMessage(queueMessage);
        } catch (final MessageQueueException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (final Exception e) {
            throw classifyException(e);
        }
    }
}
