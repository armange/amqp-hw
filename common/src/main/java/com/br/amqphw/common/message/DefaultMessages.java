package com.br.amqphw.common.message;

public enum DefaultMessages implements MessageFormat {
    THE_PARAMETER_IS_REQUIRED("The '{0}' parameter is required.");

    private final String message;
    
    DefaultMessages(final String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}
