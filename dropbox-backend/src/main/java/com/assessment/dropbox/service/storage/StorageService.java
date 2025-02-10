package com.assessment.dropbox.service.storage;

import com.assessment.dropbox.dto.UploadUrlResponse;

public interface StorageService {
    UploadUrlResponse generateUploadUrl(String fileName);

    String generateDownloadUrl(String path);
}