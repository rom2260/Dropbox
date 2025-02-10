package com.assessment.dropbox.repository;

import com.assessment.dropbox.entity.UserFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    Page<UserFile> findByUserId(Long userId, Pageable pageable);
}