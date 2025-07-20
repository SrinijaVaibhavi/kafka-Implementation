package com.example.demo.config;

import com.example.demo.service.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    @Autowired
    private EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void listen(String messagePayload) {
    	System.out.println("Received Kafka payload: " + messagePayload);
        try {
        	System.out.println("DEBUG: STARTED listen() in EmailConsumer.java!");
        	System.out.println("DEBUG: Message payload received: " + messagePayload);

            JsonNode jsonNode = objectMapper.readTree(messagePayload);

            String toEmail = jsonNode.get("toEmail").asText();
            String subject = jsonNode.get("subject").asText();
            String body = jsonNode.get("body").asText();

            // These are the values you put into Kafka when uploading
            // For example: "attachmentFileName": "file.pdf"
            // And "attachmentGcsPath": "gs://bucket-name/file.pdf"
            String attachmentFileName = null;
            String gcsBucket = null;
            String gcsFileName = null;

            JsonNode fileNameNode = jsonNode.get("attachmentFileName");
            JsonNode gcsPathNode = jsonNode.get("attachmentGcsPath");

            if (fileNameNode != null && !fileNameNode.asText().isEmpty()) {
                attachmentFileName = fileNameNode.asText();
            }

            if (gcsPathNode != null && !gcsPathNode.asText().isEmpty()) {
                // Parse the GCS path "gs://bucket-name/file.pdf"
                String gcsPath = gcsPathNode.asText();
                if (gcsPath.startsWith("gs://")) {
                    String pathWithoutPrefix = gcsPath.substring(5); // Remove 'gs://'
                    int firstSlash = pathWithoutPrefix.indexOf('/');
                    if (firstSlash != -1) {
                        gcsBucket = pathWithoutPrefix.substring(0, firstSlash);
                        gcsFileName = pathWithoutPrefix.substring(firstSlash + 1);
                    }
                }
            }
            System.out.println("attachmentFileName: " + attachmentFileName);
            System.out.println("gcsBucket: " + gcsBucket);
            System.out.println("gcsFileName: " + gcsFileName);

            System.out.println("DEBUG: To send email with -- gcsBucket: " + gcsBucket + ", gcsFileName: " + gcsFileName);
            // Send email using the GCS-based attachment
            emailService.sendEmail(toEmail, subject, body, gcsBucket, gcsFileName);

            System.out.println("Email sent to: " + toEmail);
            System.out.println("gcsBucket: " + gcsBucket + ", gcsFileName: " + gcsFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
