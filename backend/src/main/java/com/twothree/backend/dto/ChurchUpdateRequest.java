package com.twothree.backend.dto;

import lombok.Data;

@Data
public class ChurchUpdateRequest {
    private Long churchId;
    private ChurchDto churchDto;
} 