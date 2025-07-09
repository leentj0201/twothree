package com.twothree.backend.service;

import com.twothree.backend.domain.ChurchDomainService;
import com.twothree.backend.dto.ChurchDto;
import com.twothree.backend.entity.Church;
import com.twothree.backend.enums.ChurchStatus;
import com.twothree.backend.mapper.ChurchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChurchService {
    
    private final ChurchDomainService churchDomainService;
    private final ChurchMapper churchMapper;
    
    public List<ChurchDto> getAllChurches() {
        return churchMapper.toDtoList(churchDomainService.findAll());
    }
    
    public Optional<ChurchDto> getChurchById(Long id) {
        return churchDomainService.findById(id)
                .map(churchMapper::toDto);
    }
    
    public Optional<ChurchDto> getChurchByName(String name) {
        return churchDomainService.findByName(name)
                .map(churchMapper::toDto);
    }
    
    public List<ChurchDto> getChurchesByStatus(ChurchStatus status) {
        return churchMapper.toDtoList(churchDomainService.findByStatus(status));
    }
    
    public List<ChurchDto> searchChurches(String keyword) {
        return churchMapper.toDtoList(churchDomainService.searchByKeyword(keyword));
    }
    
    public ChurchDto createChurch(ChurchDto churchDto) {
        Church church = churchMapper.toEntity(churchDto);
        Church savedChurch = churchDomainService.createChurch(church);
        return churchMapper.toDto(savedChurch);
    }
    
    public Optional<ChurchDto> updateChurch(Long id, ChurchDto churchDto) {
        Church churchData = churchMapper.toEntity(churchDto);
        
        try {
            Church updatedChurch = churchDomainService.updateChurch(id, churchData);
            return Optional.of(churchMapper.toDto(updatedChurch));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
    
    public boolean deleteChurch(Long id) {
        return churchDomainService.deleteChurch(id);
    }
    
    public boolean existsByName(String name) {
        return churchDomainService.existsByName(name);
    }
    
    public boolean existsByEmail(String email) {
        return churchDomainService.existsByEmail(email);
    }
} 