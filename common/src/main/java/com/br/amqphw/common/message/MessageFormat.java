package com.br.amqphw.common.message;

public interface MessageFormat {

    String getMessage();
    
    default String format(final Object... arguments) {
        return java.text.MessageFormat.format(getMessage(), arguments);
    }
}
