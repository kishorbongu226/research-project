package com.example.demo.service.serviceImpl;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.service.FileUploadService;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    private final S3Client s3Client;

    @Override
    public String uploadFile(MultipartFile file) {
        logger.info("Starting S3 upload to bucket: {} (region: {}) | file: {}, size: {} bytes",
                bucketName, region, file.getOriginalFilename(), file.getSize());

        String filenameExtension =
                file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                logger.info("FileName extension is : {}",filenameExtension);
        String key = UUID.randomUUID().toString() + "." + filenameExtension;

        logger.info("Generated S3 object key: {} (Bucket: [{}])", key, bucketName);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse response =
                    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
                    logger.info(response.toString());

            if (response.sdkHttpResponse().isSuccessful()) {
                String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
                logger.info("File uploaded successfully to S3 bucket: {} | URL: {}", bucketName, fileUrl);
                return fileUrl;
            }

            logger.error("Failed to upload file to S3 bucket: {} | HTTP Status: {}",
                    bucketName, response.sdkHttpResponse().statusCode());

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file");

        } catch (IOException e) {
            logger.error("IOException while uploading to bucket {}: {}", bucketName, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while uploading the file");
        } catch (Exception e) {
            logger.error("Unexpected S3 upload error (bucket: {}): {}", bucketName, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error occurred");
        }
    }

    @Override
    public boolean deleteFile(String imgUrl) {
        logger.info("File delete requested from bucket: {} | URL: {}", bucketName, imgUrl);

        try {
            String filename = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);

            logger.info("Deleting file '{}' from bucket: {}", filename, bucketName);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            logger.info("File '{}' successfully deleted from bucket: {}", filename, bucketName);
            return true;

        } catch (Exception e) {
            logger.error("Failed to delete file from bucket {}: {}", bucketName, e.getMessage(), e);
            return false;
        }
    }

}
