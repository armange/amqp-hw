package com.br.amqphw.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.net.ConnectException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.br.amqphw.mqcommon.exception.MessageQueueException;
import com.br.amqphw.mqcommon.mq.QueueMessage;
import com.br.amqphw.mqcommon.mq.SenderMQ;

public class SenderServiceTest {
    private final QueueMessage queueMessage = new QueueMessage("queue", "message");
    private final SenderMQ senderMQ = mock(SenderMQ.class, RETURNS_SMART_NULLS);
    
    @BeforeEach
    public void beforeEach() {
        reset(senderMQ);
    }
    
    @Test
    public void sendMessage() {
        final SenderService service = new SenderService(senderMQ);
        
        service.sendMessage(queueMessage);
        
        verify(senderMQ).sendMessage(any());
    }
    
    @Test
    public void sendMessageAndReturnBadRequest() {
        final SenderService service = new SenderService(senderMQ);
        
        doThrow(MessageQueueException.class)
            .when(senderMQ)
            .sendMessage(queueMessage);
        
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, 
                () -> {
                    service.sendMessage(queueMessage);
                });
        
        verify(senderMQ).sendMessage(any());
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
    
    @Test
    public void sendMessageAndReturnBadGateway() {
        final SenderService service = new SenderService(senderMQ);
        final ConnectException connectException = new ConnectException();
        final RuntimeException runtimeException = new RuntimeException(connectException);
        
        doThrow(runtimeException)
            .when(senderMQ)
            .sendMessage(queueMessage);
        
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, 
                () -> {
                    service.sendMessage(queueMessage);
                });
        
        verify(senderMQ).sendMessage(any());
        
        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatus());
        assertEquals(AbstractMQService.THE_MESSAGING_MIDDLEWARE_WAS_NOT_FOUND, 
                exception.getReason());
    }
    
    @Test
    public void sendMessageAndReturnInternalServerError() {
        final SenderService service = new SenderService(senderMQ);
        
        doThrow(RuntimeException.class)
            .when(senderMQ)
            .sendMessage(queueMessage);
        
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, 
                () -> {
                    service.sendMessage(queueMessage);
                });
        
        verify(senderMQ).sendMessage(any());
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }
}
