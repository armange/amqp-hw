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
import com.br.amqphw.mqcommon.mq.ReceiverMQ;

public class ReceiverServiceTest {
    private final String message = "message";
    private final ReceiverMQ receiverMQ = mock(ReceiverMQ.class, RETURNS_SMART_NULLS);
    private final ReceiverService service = new ReceiverService(receiverMQ);
    
    @BeforeEach
    public void beforeEach() {
        reset(receiverMQ);
    }
    
    @Test
    public void sendMessage() {
        service.receiveMessage(message);
        
        verify(receiverMQ).receiveQueue(any());
    }
    
    @Test
    public void sendMessageAndReturnBadRequest() {
        doThrow(MessageQueueException.class)
            .when(receiverMQ)
            .receiveQueue(message);
        
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, 
                () -> {
                    service.receiveMessage(message);
                });
        
        verify(receiverMQ).receiveQueue(any());
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
    
    @Test
    public void sendMessageAndReturnBadGateway() {
        final ConnectException connectException = new ConnectException();
        final RuntimeException runtimeException = new RuntimeException(connectException);
        
        doThrow(runtimeException)
            .when(receiverMQ)
            .receiveQueue(message);
        
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, 
                () -> {
                    service.receiveMessage(message);
                });
        
        verify(receiverMQ).receiveQueue(any());
        
        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatus());
        assertEquals(AbstractMQService.THE_MESSAGING_MIDDLEWARE_WAS_NOT_FOUND, 
                exception.getReason());
    }
    
    @Test
    public void sendMessageAndReturnInternalServerError() {
        doThrow(RuntimeException.class)
            .when(receiverMQ)
            .receiveQueue(message);
        
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, 
                () -> {
                    service.receiveMessage(message);
                });
        
        verify(receiverMQ).receiveQueue(any());
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }
}
