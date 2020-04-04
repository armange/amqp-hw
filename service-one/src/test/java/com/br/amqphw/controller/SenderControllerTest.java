package com.br.amqphw.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
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

import com.br.amqphw.mqcommon.mq.QueueMessage;
import com.br.amqphw.mqcommon.mq.SenderMQ;
import com.br.amqphw.service.SenderService;

public class SenderControllerTest {
    private static final String SERVICE_NAME = "service";
    private static final String QUEUE_NAME = "queueName";
    private static final String MESSAGE = "message";
    private final SenderMQ senderMQ = mock(SenderMQ.class, RETURNS_SMART_NULLS);
    private final SenderService service = spy(new SenderService(senderMQ));
    private final SenderController controller = new SenderController(service, SERVICE_NAME);

    @Test
    public void postQueue() {
        final ResponseEntity<Void> response = controller.postQueue(QUEUE_NAME, MESSAGE);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        final ArgumentCaptor<QueueMessage> queueMessage = ArgumentCaptor.forClass(QueueMessage.class);
        
        Mockito.verify(service).sendMessage(queueMessage.capture());
        
        assertThat(queueMessage.getValue(),
                allOf(
                        hasProperty("queueName", is(QUEUE_NAME)),
                        hasProperty("message", is(MESSAGE)),
                        hasProperty("serviceName", is(SERVICE_NAME))
                        )
                );
    }
}
