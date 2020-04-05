package com.br.amqphw.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.amqphw.service.ReceiverService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@Api(value = "Receiver")
public class ReceiverController {
    
    private final ReceiverService receiverService;
    
    @Autowired
    public ReceiverController(final ReceiverService receiverService) {
        this.receiverService = receiverService;
    }
    
    @ApiOperation(value = "Receives a message from a queue")
    @RequestMapping(value = "/amqp/{queue_name}/receive", method = GET, produces = "application/json")
    public ResponseEntity<String> receiveQueue(@PathVariable("queue_name") final String queueName) {
        return ResponseEntity.ok(receiverService.receiveMessage(queueName));
    }
}
