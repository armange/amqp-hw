package com.br.amqphw.mqcommon.exception;

public class MessageQueueException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MessageQueueException(final Throwable throwable) {
        super(throwable);
    }
    
    public MessageQueueException(final String message) {
        super(message);
    }
    
    public MessageQueueException(final String message, final Throwable throwable) {
        super(throwable);
    }
}
