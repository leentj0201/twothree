package com.twothree.backend.mapper;

import com.twothree.backend.dto.ChurchDto;
import com.twothree.backend.entity.Church;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChurchMapper {
    
    public ChurchDto toDto(Church church) {
        if (church == null) {
            return null;
        }
        
        return ChurchDto.builder()
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
                .createdAt(church.getCreatedAt())
                .updatedAt(church.getUpdatedAt())
                .build();
    }
    
    public Church toEntity(ChurchDto churchDto) {
        if (churchDto == null) {
            return null;
        }
        
        return Church.builder()
                .id(churchDto.getId())
                .name(churchDto.getName())
                .description(churchDto.getDescription())
                .address(churchDto.getAddress())
                .phone(churchDto.getPhone())
                .email(churchDto.getEmail())
                .website(churchDto.getWebsite())
                .pastorName(churchDto.getPastorName())
                .pastorPhone(churchDto.getPastorPhone())
                .pastorEmail(churchDto.getPastorEmail())
                .logoUrl(churchDto.getLogoUrl())
                .bannerUrl(churchDto.getBannerUrl())
                .status(churchDto.getStatus())
                .build();
    }
    
    public List<ChurchDto> toDtoList(List<Church> churches) {
        if (churches == null) {
            return null;
        }
        
        return churches.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<Church> toEntityList(List<ChurchDto> churchDtos) {
        if (churchDtos == null) {
            return null;
        }
        
        return churchDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    public void updateEntityFromDto(Church church, ChurchDto churchDto) {
        if (church == null || churchDto == null) {
            return;
        }
        
        if (churchDto.getName() != null) {
            church.setName(churchDto.getName());
        }
        if (churchDto.getDescription() != null) {
            church.setDescription(churchDto.getDescription());
        }
        if (churchDto.getAddress() != null) {
            church.setAddress(churchDto.getAddress());
        }
        if (churchDto.getPhone() != null) {
            church.setPhone(churchDto.getPhone());
        }
        if (churchDto.getEmail() != null) {
            church.setEmail(churchDto.getEmail());
        }
        if (churchDto.getWebsite() != null) {
            church.setWebsite(churchDto.getWebsite());
        }
        if (churchDto.getPastorName() != null) {
            church.setPastorName(churchDto.getPastorName());
        }
        if (churchDto.getPastorPhone() != null) {
            church.setPastorPhone(churchDto.getPastorPhone());
        }
        if (churchDto.getPastorEmail() != null) {
            church.setPastorEmail(churchDto.getPastorEmail());
        }
        if (churchDto.getLogoUrl() != null) {
            church.setLogoUrl(churchDto.getLogoUrl());
        }
        if (churchDto.getBannerUrl() != null) {
            church.setBannerUrl(churchDto.getBannerUrl());
        }
        if (churchDto.getStatus() != null) {
            church.setStatus(churchDto.getStatus());
        }
    }
} 