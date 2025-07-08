package com.twothree.backend.service;

import com.twothree.backend.dto.DepartmentDto;
import com.twothree.backend.entity.Church;
import com.twothree.backend.entity.Department;
import com.twothree.backend.enums.DepartmentStatus;
import com.twothree.backend.enums.DepartmentCategory;
import com.twothree.backend.repository.ChurchRepository;
import com.twothree.backend.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final ChurchRepository churchRepository;
    
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(DepartmentDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<DepartmentDto> getDepartmentsByChurchId(Long churchId) {
        return departmentRepository.findByChurchId(churchId).stream()
                .map(DepartmentDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<DepartmentDto> getActiveDepartmentsByChurchId(Long churchId) {
        return departmentRepository.findByChurchIdAndStatus(churchId, DepartmentStatus.ACTIVE).stream()
                .map(DepartmentDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<DepartmentDto> getDepartmentsByCategory(Long churchId, DepartmentCategory category) {
        return departmentRepository.findByCategoryAndChurchId(category, churchId).stream()
                .map(DepartmentDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<DepartmentDto> getActiveDepartmentsByCategory(Long churchId, DepartmentCategory category) {
        return departmentRepository.findByCategoryAndChurchIdAndStatus(category, churchId, DepartmentStatus.ACTIVE).stream()
                .map(DepartmentDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public Optional<DepartmentDto> getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(DepartmentDto::fromEntity);
    }
    
    public List<DepartmentDto> searchDepartmentsByChurchId(String keyword, Long churchId) {
        return departmentRepository.searchByKeywordAndChurchId(keyword, churchId).stream()
                .map(DepartmentDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Church church = churchRepository.findById(departmentDto.getChurchId())
                .orElseThrow(() -> new RuntimeException("Church not found"));
        
        Department department = Department.builder()
                .name(departmentDto.getName())
                .description(departmentDto.getDescription())
                .color(departmentDto.getColor())
                .icon(departmentDto.getIcon())
                .category(departmentDto.getCategory())
                .church(church)
                .status(departmentDto.getStatus() != null ? departmentDto.getStatus() : DepartmentStatus.ACTIVE)
                .build();
        
        Department savedDepartment = departmentRepository.save(department);
        return DepartmentDto.fromEntity(savedDepartment);
    }
    
    public Optional<DepartmentDto> updateDepartment(Long id, DepartmentDto departmentDto) {
        return departmentRepository.findById(id)
                .map(department -> {
                    department.setName(departmentDto.getName());
                    department.setDescription(departmentDto.getDescription());
                    department.setColor(departmentDto.getColor());
                    department.setIcon(departmentDto.getIcon());
                    department.setCategory(departmentDto.getCategory());
                    
                    if (departmentDto.getStatus() != null) {
                        department.setStatus(departmentDto.getStatus());
                    }
                    
                    return DepartmentDto.fromEntity(departmentRepository.save(department));
                });
    }
    
    public boolean deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsByNameAndChurchId(String name, Long churchId) {
        return departmentRepository.existsByNameAndChurchId(name, churchId);
    }
} 