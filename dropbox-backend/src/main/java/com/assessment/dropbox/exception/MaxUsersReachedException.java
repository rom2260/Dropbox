package com.assessment.dropbox.exception;

public class MaxUsersReachedException extends RuntimeException {
    public MaxUsersReachedException(String message) {
        super(message);
    }
}