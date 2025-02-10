package com.assessment.dropbox.service;

import com.assessment.dropbox.model.File;
import com.assessment.dropbox.repository.FileRepository;
import com.assessment.dropbox.repository.UserFileRepository;
import com.assessment.dropbox.entity.UserFile;
import com.assessment.dropbox.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.assessment.dropbox.dto.UploadUrlResponse;
import com.assessment.dropbox.dto.UserFileResponse;
import com.assessment.dropbox.dto.FileMetadataRequest;
import com.assessment.dropbox.repository.UserRepository;
import com.assessment.dropbox.model.User;
import com.assessment.dropbox.exception.InvalidInputException;
import com.assessment.dropbox.exception.ResourceNotFoundException;
import com.assessment.dropbox.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final UserFileRepository userFileRepository;
    private final StorageService storageService;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    public UploadUrlResponse generateUploadUrl(String fileName) {
        try {
            return storageService.generateUploadUrl(fileName);
        } catch (Exception e) {
            throw new StorageException("Failed to generate upload URL for file: " + fileName, e);
        }
    }

    public String generateDownloadUrl(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new InvalidInputException("Path cannot be null or empty");
        }
        try {
            return storageService.generateDownloadUrl(path);
        } catch (Exception e) {
            throw new StorageException("Failed to generate download URL for path: " + path, e);
        }
    }

    public List<UserFileResponse> getUserFiles(Long userId, int page, int size) {
        if (userId == null) {
            throw new InvalidInputException("User ID cannot be null");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserFile> files = userFileRepository.findByUserId(userId, pageable);

            return files.getContent().stream()
                    .map(this::mapToUserFileResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving files for user: {}. Error: {}", userId, e.getMessage(), e);
            throw new StorageException("Error retrieving files for user: " + userId, e);
        }
    }

    public UserFileResponse saveFileMetadata(FileMetadataRequest request) {
        UserFile userFile = new UserFile();
        userFile.setFileName(request.getFileName());
        userFile.setS3Path(request.getS3Path());
        userFile.setUserId(request.getUserId());
        userFile.setSize(request.getSize());
        userFile.setContentType(request.getContentType());
        userFile.setUploadedTime(LocalDateTime.now());

        UserFile savedFile = userFileRepository.save(userFile);
        return mapToUserFileResponse(savedFile);
    }

    private UserFileResponse mapToUserFileResponse(UserFile userFile) {
        return new UserFileResponse(
                userFile.getId(),
                userFile.getFileName(),
                userFile.getS3Path(),
                userFile.getSize(),
                userFile.getContentType(),
                userFile.getUploadedTime());
    }
}