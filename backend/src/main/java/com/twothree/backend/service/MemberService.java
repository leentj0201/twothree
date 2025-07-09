package com.twothree.backend.service;

import com.twothree.backend.domain.MemberDomainService;
import com.twothree.backend.dto.MemberDto;
import com.twothree.backend.entity.Church;
import com.twothree.backend.entity.Department;
import com.twothree.backend.entity.Member;
import com.twothree.backend.enums.MemberStatus;
import com.twothree.backend.enums.MemberRole;
import com.twothree.backend.repository.ChurchRepository;
import com.twothree.backend.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    
    private final MemberDomainService memberDomainService;
    private final ChurchRepository churchRepository;
    private final DepartmentRepository departmentRepository;
    
    public List<MemberDto> getAllMembers() {
        return memberDomainService.findAll().stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByChurchId(Long churchId) {
        return memberDomainService.findByChurchId(churchId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public Page<MemberDto> getMembersByChurchId(Long churchId, Pageable pageable) {
        return memberDomainService.findByChurchId(churchId, pageable)
                .map(MemberDto::fromEntity);
    }
    
    public List<MemberDto> getMembersByChurchIdAndStatus(Long churchId, MemberStatus status) {
        return memberDomainService.findByChurchIdAndStatus(churchId, status).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByDepartmentId(Long departmentId) {
        return memberDomainService.findByDepartmentId(departmentId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByChurchIdAndDepartmentId(Long churchId, Long departmentId) {
        return memberDomainService.findByChurchIdAndDepartmentId(churchId, departmentId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByChurchIdAndRole(Long churchId, MemberRole role) {
        return memberDomainService.findByChurchIdAndRole(churchId, role).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public Optional<MemberDto> getMemberById(Long id) {
        return memberDomainService.findById(id)
                .map(MemberDto::fromEntity);
    }
    
    public Optional<MemberDto> getMemberByEmail(String email) {
        return memberDomainService.findByEmail(email)
                .map(MemberDto::fromEntity);
    }
    
    public List<MemberDto> searchMembersByChurchId(String keyword, Long churchId) {
        return memberDomainService.searchByKeywordAndChurchId(keyword, churchId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByBirthDateRange(Long churchId, LocalDate startDate, LocalDate endDate) {
        return memberDomainService.findByBirthDateBetweenAndChurchId(startDate, endDate, churchId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByMembershipDateRange(Long churchId, LocalDate startDate, LocalDate endDate) {
        return memberDomainService.findByMembershipDateBetweenAndChurchId(startDate, endDate, churchId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public MemberDto createMember(MemberDto memberDto) {
        Church church = churchRepository.findById(memberDto.getChurchId())
                .orElseThrow(() -> new RuntimeException("Church not found"));
        
        Department department = null;
        if (memberDto.getDepartmentId() != null) {
            department = departmentRepository.findById(memberDto.getDepartmentId())
                    .orElse(null);
        }
        
        Member member = Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .phone(memberDto.getPhone())
                .address(memberDto.getAddress())
                .birthDate(memberDto.getBirthDate())
                .gender(memberDto.getGender())
                .status(memberDto.getStatus() != null ? memberDto.getStatus() : MemberStatus.ACTIVE)
                .role(memberDto.getRole() != null ? memberDto.getRole() : MemberRole.MEMBER)
                .profileImageUrl(memberDto.getProfileImageUrl())
                .baptismDate(memberDto.getBaptismDate())
                .membershipDate(memberDto.getMembershipDate())
                .notes(memberDto.getNotes())
                .church(church)
                .department(department)
                .build();
        
        Member savedMember = memberDomainService.createMember(member);
        return MemberDto.fromEntity(savedMember);
    }
    
    public Optional<MemberDto> updateMember(Long id, MemberDto memberDto) {
        Church church = churchRepository.findById(memberDto.getChurchId())
                .orElseThrow(() -> new RuntimeException("Church not found"));
        
        Department department = null;
        if (memberDto.getDepartmentId() != null) {
            department = departmentRepository.findById(memberDto.getDepartmentId())
                    .orElse(null);
        }
        
        Member memberData = Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .phone(memberDto.getPhone())
                .address(memberDto.getAddress())
                .birthDate(memberDto.getBirthDate())
                .gender(memberDto.getGender())
                .status(memberDto.getStatus())
                .role(memberDto.getRole())
                .profileImageUrl(memberDto.getProfileImageUrl())
                .baptismDate(memberDto.getBaptismDate())
                .membershipDate(memberDto.getMembershipDate())
                .notes(memberDto.getNotes())
                .church(church)
                .department(department)
                .build();
        
        try {
            Member updatedMember = memberDomainService.updateMember(id, memberData);
            return Optional.of(MemberDto.fromEntity(updatedMember));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
    
    public boolean deleteMember(Long id) {
        return memberDomainService.deleteMember(id);
    }
    
    public boolean existsByEmail(String email) {
        return memberDomainService.existsByEmail(email);
    }
    
    public boolean existsByEmailAndChurchId(String email, Long churchId) {
        return memberDomainService.existsByEmailAndChurchId(email, churchId);
    }
} 