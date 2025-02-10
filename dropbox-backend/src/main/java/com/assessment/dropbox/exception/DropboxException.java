package com.assessment.dropbox.exception;

public class DropboxException extends RuntimeException {
    public DropboxException(String message) {
        super(message);
    }

    public DropboxException(String message, Throwable cause) {
        super(message, cause);
    }
}