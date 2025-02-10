package com.assessment.dropbox.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_files")
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String s3Path;
    private Long userId;
    private Long size;
    private String contentType;
    private LocalDateTime uploadedTime;
}