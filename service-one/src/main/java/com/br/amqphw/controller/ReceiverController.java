package com.br.amqphw.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.amqphw.service.ReceiverService;

@RestController
@CrossOrigin("*")
public class ReceiverController {
    
    private final ReceiverService receiverService;
    
    @Autowired
    public ReceiverController(final ReceiverService receiverService) {
        this.receiverService = receiverService;
    }
    
    @RequestMapping(value = "/amqp/{queue_name}/receive", method = GET, produces = "application/json")
    public ResponseEntity<String> receiveQueue(@PathVariable("queue_name") final String queueName) {
        return ResponseEntity.ok(receiverService.receiveMessage(queueName));
    }
}
