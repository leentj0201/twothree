package com.twothree.backend.dto;

import com.twothree.backend.enums.DepartmentStatus;
import com.twothree.backend.enums.DepartmentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.twothree.backend.entity.Department;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    
    private Long id;
    private String name;
    private String description;
    private String color;
    private String icon;
    private Long churchId;
    private DepartmentCategory category;
    private DepartmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    public static DepartmentDto fromEntity(Department department) {
        DepartmentDto dto = DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .color(department.getColor())
                .icon(department.getIcon())
                .churchId(department.getChurch().getId())
                .category(department.getCategory())
                .status(department.getStatus())
                .build();
        
        dto.setCreatedAt(department.getCreatedAt());
        dto.setUpdatedAt(department.getUpdatedAt());
        dto.setCreatedBy(department.getCreatedBy());
        dto.setUpdatedBy(department.getUpdatedBy());
        
        return dto;
    }
} 