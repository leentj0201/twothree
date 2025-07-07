package com.twothree.backend.dto;

import lombok.Data;

@Data
public class ContentUpdateRequest {
    private Long contentId;
    private ContentDto contentDto;
} 