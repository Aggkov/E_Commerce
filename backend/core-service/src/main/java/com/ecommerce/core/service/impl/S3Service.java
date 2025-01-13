package com.ecommerce.core.service.impl;

import java.io.IOException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final S3Client s3Client;
    private static final String BUCKET_NAME = "your_S3_name";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String key, Path filePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, filePath);
    }

    public byte[] downloadFile(String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        return s3Client.getObject(getObjectRequest).readAllBytes();
    }

//    public List<String> listFiles(String prefix) {
//        ListObjectsV2Request request = ListObjectsV2Request.builder()
//                .bucket(BUCKET_NAME)
//                .prefix(prefix)
//                .build();
//
//        ListObjectsV2Response response = s3Client.listObjectsV2(request);
//
//        return response.contents().stream()
//                .map(S3Object::key)
//                .collect(Collectors.toList());
//    }
}
