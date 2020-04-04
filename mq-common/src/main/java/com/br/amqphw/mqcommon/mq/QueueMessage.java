package com.br.amqphw.mqcommon.mq;

import java.time.OffsetDateTime;
import java.util.UUID;

public class QueueMessage {
    private final UUID id;
    private final OffsetDateTime dateTime;
    private String serviceName;
    private final String message;
    private final String queueName;
    
    public QueueMessage(final String queueName, final String message) {
        this.id = UUID.randomUUID();
        this.dateTime = OffsetDateTime.now();
        this.queueName = queueName;
        this.message = message;
    }
    
    public QueueMessage(final String queueName, final String message, final String serviceName) {
        this.id = UUID.randomUUID();
        this.dateTime = OffsetDateTime.now();
        this.serviceName = serviceName;
        this.queueName = queueName;
        this.message = message;
    }
    
    public UUID getId() {
        return id;
    }
    
    public OffsetDateTime getDateTime() {
        return dateTime;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getQueueName() {
        return queueName;
    }
}
