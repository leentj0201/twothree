package com.twothree.backend.dto;

import lombok.Data;

@Data
public class MemberEmailChurchIdRequest {
    private String email;
    private Long churchId;
} 