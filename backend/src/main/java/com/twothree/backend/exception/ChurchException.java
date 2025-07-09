package com.twothree.backend.exception;

import org.springframework.http.HttpStatus;

public class ChurchException extends BaseException {
    
    public static final String CHURCH_NOT_FOUND = "CHURCH_NOT_FOUND";
    public static final String CHURCH_NAME_EXISTS = "CHURCH_NAME_EXISTS";
    public static final String CHURCH_EMAIL_EXISTS = "CHURCH_EMAIL_EXISTS";
    public static final String CHURCH_NAME_REQUIRED = "CHURCH_NAME_REQUIRED";
    public static final String CHURCH_ADDRESS_REQUIRED = "CHURCH_ADDRESS_REQUIRED";
    public static final String CHURCH_CANNOT_DELETE_ACTIVE = "CHURCH_CANNOT_DELETE_ACTIVE";
    
    public ChurchException(String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
    
    public ChurchException(String message, String errorCode, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, errorCode, cause);
    }
    
    public static ChurchException notFound(Long id) {
        return new ChurchException(
            "Church not found with id: " + id,
            CHURCH_NOT_FOUND
        );
    }
    
    public static ChurchException nameExists(String name) {
        return new ChurchException(
            "Church name already exists: " + name,
            CHURCH_NAME_EXISTS
        );
    }
    
    public static ChurchException emailExists(String email) {
        return new ChurchException(
            "Church email already exists: " + email,
            CHURCH_EMAIL_EXISTS
        );
    }
    
    public static ChurchException nameRequired() {
        return new ChurchException(
            "Church name is required",
            CHURCH_NAME_REQUIRED
        );
    }
    
    public static ChurchException addressRequired() {
        return new ChurchException(
            "Church address is required",
            CHURCH_ADDRESS_REQUIRED
        );
    }
    
    public static ChurchException cannotDeleteActive() {
        return new ChurchException(
            "Cannot delete active church. Please deactivate first.",
            CHURCH_CANNOT_DELETE_ACTIVE
        );
    }
} 