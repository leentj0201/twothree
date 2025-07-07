package com.twothree.backend.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class ChurchIdPageRequest {
    private Long churchId;
    private Pageable pageable;
} 