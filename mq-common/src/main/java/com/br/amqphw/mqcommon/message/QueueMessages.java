package com.br.amqphw.mqcommon.message;

import com.br.amqphw.common.message.MessageFormat;

public enum QueueMessages implements MessageFormat {
    /*
     * Common
     * */
    QUEUE_NAME_REQUIRED("The queue name is required and cannot be empty"),
    QUEUE_MESSAGE_REQUIRED("The queue message is required and cannot be empty"),
    
    /*
     * Receiver
     * */
    ERROR_ON_RECEIVING_MESSAGE_FROM_QUEUE("Error on receiving message from queue '{0}'"),
    WAITING_FOR_MESSAGES_ON("Waiting for messages on '{0}'"),
    THERE_ARE_NO_MORE_MESSAGES("There are no more messages"),
    MESSAGE_RECEIVED("Message '{0}' received"),
    CONNECTED_IN("Connected in '{0}'"),
    
    /*
     * Sender
     * */
    ERROR_ON_SENDING_MESSAGE_TO_THE_QUEUE("Erro on sending message '{0}' to the queue '{1}'"),
    CONNECTING_TO_DE_MIDWARE("Connecting to de middleware {0}"),
    MESSAGE_SENT("Message '{0}' sent")
    ;

    private final String message;
    
    QueueMessages(final String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
}
