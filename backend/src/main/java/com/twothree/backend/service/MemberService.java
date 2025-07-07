package com.twothree.backend.service;

import com.twothree.backend.dto.MemberDto;
import com.twothree.backend.entity.Church;
import com.twothree.backend.entity.Department;
import com.twothree.backend.entity.Member;
import com.twothree.backend.enums.MemberStatus;
import com.twothree.backend.enums.MemberRole;
import com.twothree.backend.repository.ChurchRepository;
import com.twothree.backend.repository.DepartmentRepository;
import com.twothree.backend.repository.MemberRepository;
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
    
    private final MemberRepository memberRepository;
    private final ChurchRepository churchRepository;
    private final DepartmentRepository departmentRepository;
    
    public List<MemberDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByChurchId(Long churchId) {
        return memberRepository.findByChurchId(churchId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public Page<MemberDto> getMembersByChurchId(Long churchId, Pageable pageable) {
        return memberRepository.findByChurchId(churchId, pageable)
                .map(MemberDto::fromEntity);
    }
    
    public List<MemberDto> getMembersByChurchIdAndStatus(Long churchId, MemberStatus status) {
        return memberRepository.findByChurchIdAndStatus(churchId, status).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByDepartmentId(Long departmentId) {
        return memberRepository.findByDepartmentId(departmentId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByChurchIdAndDepartmentId(Long churchId, Long departmentId) {
        return memberRepository.findByChurchIdAndDepartmentId(churchId, departmentId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByChurchIdAndRole(Long churchId, MemberRole role) {
        return memberRepository.findByChurchIdAndRole(churchId, role).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public Optional<MemberDto> getMemberById(Long id) {
        return memberRepository.findById(id)
                .map(MemberDto::fromEntity);
    }
    
    public Optional<MemberDto> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberDto::fromEntity);
    }
    
    public List<MemberDto> searchMembersByChurchId(String keyword, Long churchId) {
        return memberRepository.searchByKeywordAndChurchId(keyword, churchId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByBirthDateRange(Long churchId, LocalDate startDate, LocalDate endDate) {
        return memberRepository.findByBirthDateBetweenAndChurchId(startDate, endDate, churchId).stream()
                .map(MemberDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<MemberDto> getMembersByMembershipDateRange(Long churchId, LocalDate startDate, LocalDate endDate) {
        return memberRepository.findByMembershipDateBetweenAndChurchId(startDate, endDate, churchId).stream()
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
        
        Member savedMember = memberRepository.save(member);
        return MemberDto.fromEntity(savedMember);
    }
    
    public Optional<MemberDto> updateMember(Long id, MemberDto memberDto) {
        return memberRepository.findById(id)
                .map(member -> {
                    member.setName(memberDto.getName());
                    member.setEmail(memberDto.getEmail());
                    member.setPhone(memberDto.getPhone());
                    member.setAddress(memberDto.getAddress());
                    member.setBirthDate(memberDto.getBirthDate());
                    member.setGender(memberDto.getGender());
                    member.setProfileImageUrl(memberDto.getProfileImageUrl());
                    member.setBaptismDate(memberDto.getBaptismDate());
                    member.setMembershipDate(memberDto.getMembershipDate());
                    member.setNotes(memberDto.getNotes());
                    
                    if (memberDto.getStatus() != null) {
                        member.setStatus(memberDto.getStatus());
                    }
                    
                    if (memberDto.getRole() != null) {
                        member.setRole(memberDto.getRole());
                    }
                    
                    if (memberDto.getDepartmentId() != null) {
                        Department department = departmentRepository.findById(memberDto.getDepartmentId())
                                .orElse(null);
                        member.setDepartment(department);
                    } else {
                        member.setDepartment(null);
                    }
                    
                    return MemberDto.fromEntity(memberRepository.save(member));
                });
    }
    
    public boolean deleteMember(Long id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
    
    public boolean existsByEmailAndChurchId(String email, Long churchId) {
        return memberRepository.existsByEmailAndChurchId(email, churchId);
    }
} 