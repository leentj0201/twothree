package com.twothree.backend.domain;

import com.twothree.backend.entity.Church;
import com.twothree.backend.enums.ChurchStatus;
import com.twothree.backend.repository.ChurchRepository;
import com.twothree.backend.validation.ChurchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChurchDomainService {
    
    private final ChurchRepository churchRepository;
    private final ChurchValidator churchValidator;
    
    /**
     * 교회 생성 비즈니스 로직
     */
    @Transactional
    public Church createChurch(Church church) {
        // 검증 및 기본값 설정
        churchValidator.validateForCreation(church);
        
        if (church.getStatus() == null) {
            church.setStatus(ChurchStatus.ACTIVE);
        }
        
        return churchRepository.save(church);
    }
    
    /**
     * 교회 수정 비즈니스 로직
     */
    @Transactional
    public Church updateChurch(Long id, Church churchData) {
        Church existingChurch = churchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Church not found with id: " + id));
        
        // 검증 및 업데이트
        churchValidator.validateForUpdate(existingChurch, churchData);
        updateChurchFields(existingChurch, churchData);
        
        return churchRepository.save(existingChurch);
    }
    
    /**
     * 교회 삭제 비즈니스 로직
     */
    @Transactional
    public boolean deleteChurch(Long id) {
        Church church = churchRepository.findById(id)
                .orElse(null);
        
        if (church == null) {
            return false;
        }
        
        // 삭제 가능 여부 검증
        churchValidator.validateForDeletion(church);
        
        churchRepository.delete(church);
        return true;
    }
    
    /**
     * 교회 조회 - 단일
     */
    public Optional<Church> findById(Long id) {
        return churchRepository.findById(id);
    }
    
    /**
     * 교회 조회 - 이름으로
     */
    public Optional<Church> findByName(String name) {
        return churchRepository.findByName(name);
    }
    
    /**
     * 교회 조회 - 상태별
     */
    public List<Church> findByStatus(ChurchStatus status) {
        return churchRepository.findByStatus(status);
    }
    
    /**
     * 교회 조회 - 키워드 검색
     */
    public List<Church> searchByKeyword(String keyword) {
        return churchRepository.searchByKeyword(keyword);
    }
    
    /**
     * 교회 조회 - 전체
     */
    public List<Church> findAll() {
        return churchRepository.findAll();
    }
    
    /**
     * 교회 존재 여부 확인 - 이름
     */
    public boolean existsByName(String name) {
        return churchRepository.existsByName(name);
    }
    
    /**
     * 교회 존재 여부 확인 - 이메일
     */
    public boolean existsByEmail(String email) {
        return churchRepository.existsByEmail(email);
    }
    
    // ========== 비즈니스 규칙 검증 메서드들 ==========
    
    /**
     * 교회 생성 시 비즈니스 규칙 검증
     */
    private void validateChurchCreation(Church church) {
        // 이름 중복 검증
        if (existsByName(church.getName())) {
            throw new RuntimeException("Church name already exists: " + church.getName());
        }
        
        // 이메일 중복 검증
        if (church.getEmail() != null && existsByEmail(church.getEmail())) {
            throw new RuntimeException("Church email already exists: " + church.getEmail());
        }
        
        // 필수 필드 검증
        if (church.getName() == null || church.getName().trim().isEmpty()) {
            throw new RuntimeException("Church name is required");
        }
        
        if (church.getAddress() == null || church.getAddress().trim().isEmpty()) {
            throw new RuntimeException("Church address is required");
        }
    }
    
    /**
     * 교회 수정 시 비즈니스 규칙 검증
     */
    private void validateChurchUpdate(Church existingChurch, Church churchData) {
        // 이름 변경 시 중복 검증
        if (!existingChurch.getName().equals(churchData.getName()) && 
            existsByName(churchData.getName())) {
            throw new RuntimeException("Church name already exists: " + churchData.getName());
        }
        
        // 이메일 변경 시 중복 검증
        if (churchData.getEmail() != null && 
            !churchData.getEmail().equals(existingChurch.getEmail()) && 
            existsByEmail(churchData.getEmail())) {
            throw new RuntimeException("Church email already exists: " + churchData.getEmail());
        }
    }
    
    /**
     * 교회 삭제 시 비즈니스 규칙 검증
     */
    private void validateChurchDeletion(Church church) {
        // 활성 상태인 교회는 삭제 불가 (예시 규칙)
        if (church.getStatus() == ChurchStatus.ACTIVE) {
            throw new RuntimeException("Cannot delete active church. Please deactivate first.");
        }
        
        // 멤버가 있는 교회는 삭제 불가 (실제로는 멤버 수 확인 필요)
        // if (church.getMembers().size() > 0) {
        //     throw new RuntimeException("Cannot delete church with members");
        // }
    }
    
    /**
     * 교회 필드 업데이트
     */
    private void updateChurchFields(Church existingChurch, Church churchData) {
        if (churchData.getName() != null) {
            existingChurch.setName(churchData.getName());
        }
        if (churchData.getDescription() != null) {
            existingChurch.setDescription(churchData.getDescription());
        }
        if (churchData.getAddress() != null) {
            existingChurch.setAddress(churchData.getAddress());
        }
        if (churchData.getPhone() != null) {
            existingChurch.setPhone(churchData.getPhone());
        }
        if (churchData.getEmail() != null) {
            existingChurch.setEmail(churchData.getEmail());
        }
        if (churchData.getWebsite() != null) {
            existingChurch.setWebsite(churchData.getWebsite());
        }
        if (churchData.getPastorName() != null) {
            existingChurch.setPastorName(churchData.getPastorName());
        }
        if (churchData.getPastorPhone() != null) {
            existingChurch.setPastorPhone(churchData.getPastorPhone());
        }
        if (churchData.getPastorEmail() != null) {
            existingChurch.setPastorEmail(churchData.getPastorEmail());
        }
        if (churchData.getLogoUrl() != null) {
            existingChurch.setLogoUrl(churchData.getLogoUrl());
        }
        if (churchData.getBannerUrl() != null) {
            existingChurch.setBannerUrl(churchData.getBannerUrl());
        }
        if (churchData.getStatus() != null) {
            existingChurch.setStatus(churchData.getStatus());
        }
    }
} 