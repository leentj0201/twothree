package com.twothree.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContentDto {
    private Long id;
    private String title;
    private String body;
    private Long authorId;
    private Long churchId;
    private List<Long> departmentIds; // 공개범위
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 