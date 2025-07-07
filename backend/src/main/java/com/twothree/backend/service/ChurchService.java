package com.twothree.backend.service;

import com.twothree.backend.dto.ChurchDto;
import com.twothree.backend.entity.Church;
import com.twothree.backend.enums.ChurchStatus;
import com.twothree.backend.repository.ChurchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChurchService {
    
    private final ChurchRepository churchRepository;
    
    public List<ChurchDto> getAllChurches() {
        return churchRepository.findAll().stream()
                .map(ChurchDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public Optional<ChurchDto> getChurchById(Long id) {
        return churchRepository.findById(id)
                .map(ChurchDto::fromEntity);
    }
    
    public Optional<ChurchDto> getChurchByName(String name) {
        return churchRepository.findByName(name)
                .map(ChurchDto::fromEntity);
    }
    
    public List<ChurchDto> getChurchesByStatus(ChurchStatus status) {
        return churchRepository.findByStatus(status).stream()
                .map(ChurchDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<ChurchDto> searchChurches(String keyword) {
        return churchRepository.searchByKeyword(keyword).stream()
                .map(ChurchDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public ChurchDto createChurch(ChurchDto churchDto) {
        Church church = Church.builder()
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
                .status(churchDto.getStatus() != null ? churchDto.getStatus() : ChurchStatus.ACTIVE)
                .build();
        
        Church savedChurch = churchRepository.save(church);
        return ChurchDto.fromEntity(savedChurch);
    }
    
    public Optional<ChurchDto> updateChurch(Long id, ChurchDto churchDto) {
        return churchRepository.findById(id)
                .map(church -> {
                    church.setName(churchDto.getName());
                    church.setDescription(churchDto.getDescription());
                    church.setAddress(churchDto.getAddress());
                    church.setPhone(churchDto.getPhone());
                    church.setEmail(churchDto.getEmail());
                    church.setWebsite(churchDto.getWebsite());
                    church.setPastorName(churchDto.getPastorName());
                    church.setPastorPhone(churchDto.getPastorPhone());
                    church.setPastorEmail(churchDto.getPastorEmail());
                    church.setLogoUrl(churchDto.getLogoUrl());
                    church.setBannerUrl(churchDto.getBannerUrl());
                    if (churchDto.getStatus() != null) {
                        church.setStatus(churchDto.getStatus());
                    }
                    return ChurchDto.fromEntity(churchRepository.save(church));
                });
    }
    
    public boolean deleteChurch(Long id) {
        if (churchRepository.existsById(id)) {
            churchRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsByName(String name) {
        return churchRepository.existsByName(name);
    }
    
    public boolean existsByEmail(String email) {
        return churchRepository.existsByEmail(email);
    }
} 