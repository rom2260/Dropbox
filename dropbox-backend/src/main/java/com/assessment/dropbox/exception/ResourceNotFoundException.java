package com.assessment.dropbox.exception;

public class ResourceNotFoundException extends DropboxException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}