package com.assessment.dropbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFileResponse {
    private Long id;
    private String fileName;
    private String s3Path;
    private Long size;
    private String contentType;
    private LocalDateTime uploadedTime;
}