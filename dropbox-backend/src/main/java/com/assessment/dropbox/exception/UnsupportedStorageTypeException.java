package com.assessment.dropbox.exception;

public class UnsupportedStorageTypeException extends RuntimeException {
    public UnsupportedStorageTypeException(String storageType) {
        super("Storage type '" + storageType + "' is not supported");
    }
}