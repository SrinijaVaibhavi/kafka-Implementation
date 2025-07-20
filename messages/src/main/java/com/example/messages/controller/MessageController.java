package com.example.messages.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.messages.entity.MessageRecord;
import com.example.messages.repository.MessageRecordRepository;
import com.example.messages.config.KafkaProducer;
import com.example.messages.service.GcsService;

@CrossOrigin(origins = "http://localhost:4200")  // Allow requests from Angular dev server
@RestController
public class MessageController {

    @Autowired
    private KafkaProducer kafkaProducer;
    
    @Autowired
    private MessageRecordRepository messageRecordRepository;
    
    @Autowired
    private GcsService gcsService;

    @PostMapping(path = "/api/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> receiveMessage(
        @RequestParam("firstName") String firstName,
        @RequestParam("lastName") String lastName,
        @RequestParam("email") String email,
        @RequestParam("subject") String subject,
        @RequestParam("message") String message,
        @RequestPart(value = "attachment", required = false) MultipartFile attachment
    ) {
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);

        String attachmentFileName = null;
        String gcsFilePath = null;

        try {
            if (attachment != null && !attachment.isEmpty()) {
                attachmentFileName = attachment.getOriginalFilename();
                gcsFilePath = gcsService.uploadFile(attachment, attachmentFileName);
                System.out.println("Uploaded to GCS: " + gcsFilePath);
            } else {
                System.out.println("No file attached");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload attachment");
        }
        
        // Send to Kafka (with GCS file path)
        String kafkaMessage = String.format(
            "{\"toEmail\":\"%s\", \"subject\":\"%s\", \"body\":\"%s\", \"attachmentFileName\":\"%s\", \"attachmentGcsPath\":\"%s\"}",
            email, subject, message,
            attachmentFileName == null ? "" : attachmentFileName,
            gcsFilePath == null ? "" : gcsFilePath
        );
        
        MessageRecord record = new MessageRecord();
        record.setFirstName(firstName);
        record.setLastName(lastName);
        record.setEmail(email);
        record.setSubject(subject);
        record.setMessage(message);
        record.setAttachmentFileName(attachmentFileName);
        record.setAttachmentGcsPath(gcsFilePath);
        
        CompletableFuture<SendResult<String, String>> future = kafkaProducer.sendMessageWithFuture(kafkaMessage);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                // success
                record.setStatus("Kafka event published successfully");
                messageRecordRepository.save(record);
                System.out.println("Kafka message published successfully");
            } else {
                // failure
                record.setStatus("Kafka event publish failed");
                messageRecordRepository.save(record);
                System.err.println("Failed to publish Kafka message: " + ex.getMessage());
            }
        });

        return ResponseEntity.ok("Message received, saved, and sent to Kafka");
    }
}
