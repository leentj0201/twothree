package com.twothree.backend.controller.v1;

import com.twothree.backend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1")
public abstract class BaseApiController {

    protected <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> error(String message) {
        return ResponseEntity.badRequest().body(ApiResponse.error(message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> error(String message, int statusCode) {
        return ResponseEntity.status(statusCode).body(ApiResponse.error(message));
    }
} 