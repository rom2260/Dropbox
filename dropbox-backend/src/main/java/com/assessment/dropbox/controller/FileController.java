package com.assessment.dropbox.controller;

import com.assessment.dropbox.model.File;
import com.assessment.dropbox.service.FileService;
import com.assessment.dropbox.dto.UserFileResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.assessment.dropbox.constants.ApiPaths.*;
import static com.assessment.dropbox.constants.RequestParams.*;
import com.assessment.dropbox.dto.UploadUrlRequest;
import com.assessment.dropbox.dto.UploadUrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import java.util.List;
import com.assessment.dropbox.dto.FileMetadataRequest;
import com.assessment.dropbox.exception.ResourceNotFoundException;
import com.assessment.dropbox.exception.StorageException;
import com.assessment.dropbox.dto.DownloadUrlResponse;

@RestController
@RequestMapping(API_BASE_PATH + "/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {
    private final FileService fileService;
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Operation(summary = "Generate upload URL", description = "Generates a pre-signed URL for uploading a file to S3")
    @PostMapping(UPLOAD_URL_PATH)
    public ResponseEntity<UploadUrlResponse> getUploadUrl(@RequestBody UploadUrlRequest request) {
        return ResponseEntity.ok(fileService.generateUploadUrl(request.getFileName()));
    }

    @Operation(summary = "Generate download URL", description = "Generates a pre-signed URL for downloading a file from S3")
    @GetMapping(DOWNLOAD_URL_PATH)
    public ResponseEntity<DownloadUrlResponse> getDownloadUrl(@RequestParam(S3_PATH) String s3Path) {
        return ResponseEntity.ok(new DownloadUrlResponse(fileService.generateDownloadUrl(s3Path)));
    }

    @Operation(summary = "Get user files", description = "Retrieves a list of files for a specific user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserFileResponse>> getUserFiles(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<UserFileResponse> files = fileService.getUserFiles(userId, page, size);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            log.error("Error getting user files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Save file metadata", description = "Saves file metadata after successful upload to S3")
    @PostMapping("/metadata")
    public ResponseEntity<UserFileResponse> saveFileMetadata(@RequestBody FileMetadataRequest request) {
        try {
            UserFileResponse savedFile = fileService.saveFileMetadata(request);
            return ResponseEntity.ok(savedFile);
        } catch (Exception e) {
            log.error("Error saving file metadata", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}