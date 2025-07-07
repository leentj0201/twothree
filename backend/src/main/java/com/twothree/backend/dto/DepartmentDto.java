package com.twothree.backend.dto;

import com.twothree.backend.enums.DepartmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private Long parentDepartmentId;
    private List<DepartmentDto> subDepartments;
    private DepartmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    public static DepartmentDto fromEntity(com.twothree.backend.entity.Department department) {
        DepartmentDto dto = DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .color(department.getColor())
                .icon(department.getIcon())
                .churchId(department.getChurch().getId())
                .parentDepartmentId(department.getParentDepartment() != null ? department.getParentDepartment().getId() : null)
                .subDepartments(department.getSubDepartments() != null ? 
                    department.getSubDepartments().stream()
                        .map(DepartmentDto::fromEntity)
                        .collect(Collectors.toList()) : null)
                .status(department.getStatus())
                .build();
        
        dto.setCreatedAt(department.getCreatedAt());
        dto.setUpdatedAt(department.getUpdatedAt());
        dto.setCreatedBy(department.getCreatedBy());
        dto.setUpdatedBy(department.getUpdatedBy());
        
        return dto;
    }
} 