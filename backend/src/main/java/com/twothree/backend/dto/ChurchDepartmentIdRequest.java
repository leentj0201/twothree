package com.twothree.backend.dto;

import lombok.Data;

@Data
public class ChurchDepartmentIdRequest {
    private Long churchId;
    private Long departmentId;
} 