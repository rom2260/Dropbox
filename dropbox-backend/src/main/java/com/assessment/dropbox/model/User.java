package com.assessment.dropbox.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "uk_username", columnNames = "username")
})
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;
}