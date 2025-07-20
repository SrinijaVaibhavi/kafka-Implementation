package com.example.messages.config;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "email-topic";

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
    
    public CompletableFuture<SendResult<String, String>> sendMessageWithFuture(String message) {
        return kafkaTemplate.send(TOPIC, message);
    }
}