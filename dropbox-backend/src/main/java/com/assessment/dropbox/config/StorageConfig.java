package com.assessment.dropbox.config;

import com.assessment.dropbox.constants.StorageType;
import com.assessment.dropbox.service.storage.S3StorageService;
import com.assessment.dropbox.service.storage.StorageService;
import com.assessment.dropbox.exception.UnsupportedStorageTypeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${storage.type}")
    private StorageType storageType;

    @Bean
    public StorageService storageService(S3StorageService s3StorageService) {
        return switch (storageType) {
            case S3 -> s3StorageService;
            // Add more cases for different storage types
            default -> throw new UnsupportedStorageTypeException(storageType.toString());
        };
    }
}