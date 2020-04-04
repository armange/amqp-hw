package com.br.amqphw.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.br.amqphw.mqcommon.mq.ReceiverMQ;
import com.br.amqphw.service.ReceiverService;

public class ReceiverControllerTest {
    private static final String QUEUE_NAME = "queueName";
    private final ReceiverMQ receiverMQ = mock(ReceiverMQ.class, RETURNS_SMART_NULLS);
    private final ReceiverService service = spy(new ReceiverService(receiverMQ));
    private final ReceiverController controller = new ReceiverController(service);

    @Test
    public void postQueue() {
        final ResponseEntity<String> response = controller.receiveQueue(QUEUE_NAME);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        final ArgumentCaptor<String> queue = ArgumentCaptor.forClass(String.class);
        
        Mockito.verify(service).receiveMessage(queue.capture());
        
        assertThat(queue.getValue(), is(QUEUE_NAME));
    }
}
