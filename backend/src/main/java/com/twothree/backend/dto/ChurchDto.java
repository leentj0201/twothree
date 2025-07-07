package com.twothree.backend.dto;

import com.twothree.backend.enums.ChurchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChurchDto {
    
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String pastorName;
    private String pastorPhone;
    private String pastorEmail;
    private String logoUrl;
    private String bannerUrl;
    private ChurchStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    public static ChurchDto fromEntity(com.twothree.backend.entity.Church church) {
        ChurchDto dto = ChurchDto.builder()
                .id(church.getId())
                .name(church.getName())
                .description(church.getDescription())
                .address(church.getAddress())
                .phone(church.getPhone())
                .email(church.getEmail())
                .website(church.getWebsite())
                .pastorName(church.getPastorName())
                .pastorPhone(church.getPastorPhone())
                .pastorEmail(church.getPastorEmail())
                .logoUrl(church.getLogoUrl())
                .bannerUrl(church.getBannerUrl())
                .status(church.getStatus())
                .build();
        
        dto.setCreatedAt(church.getCreatedAt());
        dto.setUpdatedAt(church.getUpdatedAt());
        dto.setCreatedBy(church.getCreatedBy());
        dto.setUpdatedBy(church.getUpdatedBy());
        
        return dto;
    }
} 