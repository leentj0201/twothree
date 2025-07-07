package com.twothree.backend.dto;

import lombok.Data;

@Data
public class DepartmentUpdateRequest {
    private Long departmentId;
    private DepartmentDto departmentDto;
} 