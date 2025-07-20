package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

// Google Cloud Storage imports
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Send email with optional GCS file attachment.
     *
     * @param toEmail      Email recipient
     * @param subject      Email subject
     * @param body         Email body (text)
     * @param gcsBucket    GCS bucket name (optional, can be null/empty)
     * @param gcsFileName  File name in GCS (optional, can be null/empty)
     */
    public void sendEmail(
            String toEmail,
            String subject,
            String body,
            String gcsBucket,
            String gcsFileName
    ) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();

        ByteArrayResource attachmentResource = null;

        // Only try to get attachment if both bucket and file name are provided
        if (gcsBucket != null && !gcsBucket.isEmpty()
                && gcsFileName != null && !gcsFileName.isEmpty()) {

            // USE SERVICE ACCOUNT CREDENTIALS (!!!)
        	System.out.println("DEBUG: Creating Storage client with service account!");
            Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(
                    new FileInputStream("/Users/srinijavaibhavi/Downloads/message-app-project-464401-99860bd4f7f0.json")
                ))
                .build()
                .getService();

            Blob blob = storage.get(gcsBucket, gcsFileName);

            if (blob != null && blob.exists()) {
                attachmentResource = new ByteArrayResource(blob.getContent());
            } else {
                System.err.println("File not found in GCS: " + gcsBucket + "/" + gcsFileName);
            }
        }

        MimeMessageHelper helper = new MimeMessageHelper(message, attachmentResource != null);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body);

        if (attachmentResource != null) {
            helper.addAttachment(gcsFileName, attachmentResource);
        }

        mailSender.send(message);
    }
}
