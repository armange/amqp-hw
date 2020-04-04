package com.br.amqphw.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

abstract class AbstractMQService {
    static final String THE_MESSAGING_MIDDLEWARE_WAS_NOT_FOUND = "The messaging middleware(server) was not found";
    
    protected ResponseStatusException classifyException(final Exception exception) {
        if (exception.getCause() instanceof java.net.ConnectException) {
            return new ResponseStatusException(HttpStatus.BAD_GATEWAY, THE_MESSAGING_MIDDLEWARE_WAS_NOT_FOUND, exception);
        } else {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
