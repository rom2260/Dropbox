package com.assessment.dropbox.service;

import com.assessment.dropbox.dto.UserDto;
import com.assessment.dropbox.exception.ResourceNotFoundException;
import com.assessment.dropbox.model.User;
import com.assessment.dropbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.assessment.dropbox.constants.UserConstants;
import com.assessment.dropbox.exception.MaxUsersReachedException;
import com.assessment.dropbox.exception.DuplicateResourceException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(UserDto userDto) {
        log.info("Creating new user with username: {}", userDto.getUsername());
        long userCount = userRepository.count();
        if (userCount >= UserConstants.MAX_USERS_ALLOWED) {
            log.error("Cannot create user. Max user limit ({}) reached", UserConstants.MAX_USERS_ALLOWED);
            throw new MaxUsersReachedException("Maximum number of users (" + UserConstants.MAX_USERS_ALLOWED + ") has been reached");
        }
        try {
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            User savedUser = userRepository.save(user);
            log.info("Successfully created user with id: {}", savedUser.getId());
            return savedUser;
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to create user with username: {}", userDto.getUsername(), e);
            throw new DuplicateResourceException("Username already exists: " + userDto.getUsername());
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
        log.info("Deleted user with id: {}", userId);
    }
}