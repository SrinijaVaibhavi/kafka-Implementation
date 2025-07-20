package com.example.messages.service;

import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GcsService {

    private final String bucketName = "message-app-attachments-9"; // your bucket name

    private final Storage storage;

    // This part runs when your project starts
    public GcsService() throws IOException {
        // CHANGE THIS: Put the real path to your secret key here!
        storage = StorageOptions.newBuilder()
            .setCredentials(ServiceAccountCredentials.fromStream(
                new FileInputStream("/Users/srinijavaibhavi/Downloads/message-app-project-464401-99860bd4f7f0.json")
            ))
            .build()
            .getService();
    }

    // This part puts the file in Google’s cloud
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, file.getBytes());
        return String.format("gs://%s/%s", bucketName, fileName);
    }
}
