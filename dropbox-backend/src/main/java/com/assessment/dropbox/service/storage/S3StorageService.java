package com.assessment.dropbox.service.storage;

import com.assessment.dropbox.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.assessment.dropbox.constants.S3Constants.PRESIGNED_URL_EXPIRATION_MINUTES;
import com.assessment.dropbox.dto.UploadUrlResponse;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import com.assessment.dropbox.constants.S3Constants;

@Slf4j
@Component
public class S3StorageService implements StorageService {
    private final S3Presigner s3Presigner;
    private final String bucketName;
    private final TikaConfig tikaConfig;

    public S3StorageService(S3Presigner s3Presigner, @Value("${aws.s3.bucket}") String bucketName) {
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
        this.tikaConfig = TikaConfig.getDefaultConfig();
    }

    @Override
    public UploadUrlResponse generateUploadUrl(String fileName) {
        String contentType = detectContentType(fileName);
        String key = generateS3Key(fileName, contentType);
        log.info("Generating upload URL for file: {}, content-type: {}", fileName, contentType);
        try {
            PutObjectRequest putObjectRequest = buildPutObjectRequest(key, contentType);
            String url = generatePresignedUrl(putObjectRequest);
            log.info("Successfully generated upload URL for key: {}", key);
            return new UploadUrlResponse(key, url);
        } catch (Exception e) {
            handleUploadError(fileName, e);
            throw new StorageException("Failed to generate upload URL", e);
        }
    }

    private PutObjectRequest buildPutObjectRequest(String key, String contentType) {
        PutObjectRequest.Builder builder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType);

        return builder.build();
    }

    private String generatePresignedUrl(PutObjectRequest putObjectRequest) {
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(PRESIGNED_URL_EXPIRATION_MINUTES))
                .putObjectRequest(putObjectRequest)
                .build();
        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    private String detectContentType(String fileName) {
        try {
            Metadata metadata = new Metadata();
            metadata.set("resourceName", fileName);
            MediaType mediaType = tikaConfig.getDetector().detect(null, metadata);
            return mediaType.toString();
        } catch (Exception e) {
            log.warn("Failed to detect content type for {}, using default", fileName);
            return S3Constants.DEFAULT_CONTENT_TYPE;
        }
    }

    private String generateS3Key(String fileName, String contentType) {
        String baseFolder = contentType.split("/")[0];
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("%s/%s/%s/%s", baseFolder, timestamp, UUID.randomUUID(), fileName);
    }

    private void handleUploadError(String fileName, Exception e) {
        log.error("Failed to generate upload URL for file: {}", fileName, e);
    }

    @Override
    public String generateDownloadUrl(String s3Path) {
        log.info("Generating download URL for path: {}", s3Path);
        try {
            return generatePresignedDownloadUrl(s3Path);
        } catch (Exception e) {
            handleDownloadError(s3Path, e);
            throw new StorageException("Failed to generate download URL", e);
        }
    }

    private String generatePresignedDownloadUrl(String s3Path) {
        var getObjectRequest = software.amazon.awssdk.services.s3.model.GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(PRESIGNED_URL_EXPIRATION_MINUTES))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    private void handleDownloadError(String s3Path, Exception e) {
        log.error("Failed to generate download URL for path: {}", s3Path, e);
    }
}