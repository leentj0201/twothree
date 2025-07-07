package com.twothree.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ContentSearchRequest {
    private String keyword;         // 제목/본문/작성자명 등
    private Long authorId;
    private Long departmentId;
    private Long churchId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String sortBy;          // createdAt, title 등
    private String sortDir;         // ASC, DESC
    private Integer page;
    private Integer size;
} 