package com.assessment.dropbox.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataRequest {
    private String fileName;
    private String s3Path;
    private Long userId;
    private Long size;
    private String contentType;
}