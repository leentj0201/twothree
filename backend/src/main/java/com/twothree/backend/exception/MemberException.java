package com.twothree.backend.exception;

import org.springframework.http.HttpStatus;

public class MemberException extends BaseException {
    
    public static final String MEMBER_NOT_FOUND = "MEMBER_NOT_FOUND";
    public static final String MEMBER_EMAIL_EXISTS = "MEMBER_EMAIL_EXISTS";
    public static final String MEMBER_NAME_REQUIRED = "MEMBER_NAME_REQUIRED";
    public static final String MEMBER_EMAIL_REQUIRED = "MEMBER_EMAIL_REQUIRED";
    public static final String MEMBER_INVALID_EMAIL = "MEMBER_INVALID_EMAIL";
    public static final String MEMBER_INVALID_BIRTH_DATE = "MEMBER_INVALID_BIRTH_DATE";
    public static final String MEMBER_INVALID_BAPTISM_DATE = "MEMBER_INVALID_BAPTISM_DATE";
    public static final String MEMBER_INVALID_MEMBERSHIP_DATE = "MEMBER_INVALID_MEMBERSHIP_DATE";
    public static final String MEMBER_DEPARTMENT_NOT_FOUND = "MEMBER_DEPARTMENT_NOT_FOUND";
    public static final String MEMBER_DEPARTMENT_MISMATCH = "MEMBER_DEPARTMENT_MISMATCH";
    public static final String MEMBER_PASTOR_LIMIT_EXCEEDED = "MEMBER_PASTOR_LIMIT_EXCEEDED";
    public static final String MEMBER_CANNOT_DELETE_PASTOR = "MEMBER_CANNOT_DELETE_PASTOR";
    public static final String MEMBER_CANNOT_DELETE_ACTIVE = "MEMBER_CANNOT_DELETE_ACTIVE";
    
    public MemberException(String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
    
    public MemberException(String message, String errorCode, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, errorCode, cause);
    }
    
    public static MemberException notFound(Long id) {
        return new MemberException(
            "Member not found with id: " + id,
            MEMBER_NOT_FOUND
        );
    }
    
    public static MemberException emailExists(String email) {
        return new MemberException(
            "Email already exists in this church: " + email,
            MEMBER_EMAIL_EXISTS
        );
    }
    
    public static MemberException nameRequired() {
        return new MemberException(
            "Member name is required",
            MEMBER_NAME_REQUIRED
        );
    }
    
    public static MemberException emailRequired() {
        return new MemberException(
            "Member email is required",
            MEMBER_EMAIL_REQUIRED
        );
    }
    
    public static MemberException invalidEmail() {
        return new MemberException(
            "Invalid email format",
            MEMBER_INVALID_EMAIL
        );
    }
    
    public static MemberException invalidBirthDate() {
        return new MemberException(
            "Birth date cannot be in the future",
            MEMBER_INVALID_BIRTH_DATE
        );
    }
    
    public static MemberException invalidBaptismDate() {
        return new MemberException(
            "Baptism date cannot be before birth date",
            MEMBER_INVALID_BAPTISM_DATE
        );
    }
    
    public static MemberException invalidMembershipDate() {
        return new MemberException(
            "Membership date cannot be before birth date",
            MEMBER_INVALID_MEMBERSHIP_DATE
        );
    }
    
    public static MemberException departmentNotFound(Long departmentId) {
        return new MemberException(
            "Department not found with id: " + departmentId,
            MEMBER_DEPARTMENT_NOT_FOUND
        );
    }
    
    public static MemberException departmentMismatch() {
        return new MemberException(
            "Department does not belong to the specified church",
            MEMBER_DEPARTMENT_MISMATCH
        );
    }
    
    public static MemberException pastorLimitExceeded() {
        return new MemberException(
            "Only one pastor is allowed per church",
            MEMBER_PASTOR_LIMIT_EXCEEDED
        );
    }
    
    public static MemberException cannotDeletePastor() {
        return new MemberException(
            "Cannot delete pastor. Please transfer pastor role first.",
            MEMBER_CANNOT_DELETE_PASTOR
        );
    }
    
    public static MemberException cannotDeleteActive() {
        return new MemberException(
            "Cannot delete active member. Please deactivate first.",
            MEMBER_CANNOT_DELETE_ACTIVE
        );
    }
} 