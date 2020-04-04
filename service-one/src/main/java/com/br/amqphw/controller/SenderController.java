package com.br.amqphw.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.amqphw.mqcommon.mq.QueueMessage;
import com.br.amqphw.service.SenderService;

@RestController
@CrossOrigin("*")
public class SenderController {
    
    private final SenderService senderService;
    private final String serviceName;
    
    @Autowired
    public SenderController(
            final SenderService receiverService,
            @Value("${br.com.amqp.hw.server.name}") final String serviceName) {
        this.senderService = receiverService;
        this.serviceName = serviceName;
    }
    
    @RequestMapping(value = "/amqp/{queue_name}/send", method = POST)
    public ResponseEntity<Void> postQueue(
            @PathVariable("queue_name") final String queueName, 
            @RequestBody final String message) {
        senderService.sendMessage(new QueueMessage(queueName, message, serviceName));
        
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
