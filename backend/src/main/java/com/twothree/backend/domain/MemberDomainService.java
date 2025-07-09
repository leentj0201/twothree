package com.twothree.backend.domain;

import com.twothree.backend.entity.Church;
import com.twothree.backend.entity.Department;
import com.twothree.backend.entity.Member;
import com.twothree.backend.enums.MemberRole;
import com.twothree.backend.enums.MemberStatus;
import com.twothree.backend.repository.ChurchRepository;
import com.twothree.backend.repository.DepartmentRepository;
import com.twothree.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberDomainService {
    
    private final MemberRepository memberRepository;
    private final ChurchRepository churchRepository;
    private final DepartmentRepository departmentRepository;
    
    /**
     * 멤버 생성 비즈니스 로직
     */
    @Transactional
    public Member createMember(Member member) {
        // 비즈니스 규칙 검증
        validateMemberCreation(member);
        
        // 기본값 설정
        if (member.getStatus() == null) {
            member.setStatus(MemberStatus.ACTIVE);
        }
        if (member.getRole() == null) {
            member.setRole(MemberRole.MEMBER);
        }
        
        return memberRepository.save(member);
    }
    
    /**
     * 멤버 수정 비즈니스 로직
     */
    @Transactional
    public Member updateMember(Long id, Member memberData) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        
        // 비즈니스 규칙 검증
        validateMemberUpdate(existingMember, memberData);
        
        // 엔티티 업데이트
        updateMemberFields(existingMember, memberData);
        
        return memberRepository.save(existingMember);
    }
    
    /**
     * 멤버 삭제 비즈니스 로직
     */
    @Transactional
    public boolean deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElse(null);
        
        if (member == null) {
            return false;
        }
        
        // 삭제 가능 여부 검증
        validateMemberDeletion(member);
        
        memberRepository.delete(member);
        return true;
    }
    
    // ========== 조회 메서드들 ==========
    
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
    
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
    
    public List<Member> findByChurchId(Long churchId) {
        return memberRepository.findByChurchId(churchId);
    }
    
    public Page<Member> findByChurchId(Long churchId, Pageable pageable) {
        return memberRepository.findByChurchId(churchId, pageable);
    }
    
    public List<Member> findByChurchIdAndStatus(Long churchId, MemberStatus status) {
        return memberRepository.findByChurchIdAndStatus(churchId, status);
    }
    
    public List<Member> findByDepartmentId(Long departmentId) {
        return memberRepository.findByDepartmentId(departmentId);
    }
    
    public List<Member> findByChurchIdAndDepartmentId(Long churchId, Long departmentId) {
        return memberRepository.findByChurchIdAndDepartmentId(churchId, departmentId);
    }
    
    public List<Member> findByChurchIdAndRole(Long churchId, MemberRole role) {
        return memberRepository.findByChurchIdAndRole(churchId, role);
    }
    
    public List<Member> searchByKeywordAndChurchId(String keyword, Long churchId) {
        return memberRepository.searchByKeywordAndChurchId(keyword, churchId);
    }
    
    public List<Member> findByBirthDateBetweenAndChurchId(LocalDate startDate, LocalDate endDate, Long churchId) {
        return memberRepository.findByBirthDateBetweenAndChurchId(startDate, endDate, churchId);
    }
    
    public List<Member> findByMembershipDateBetweenAndChurchId(LocalDate startDate, LocalDate endDate, Long churchId) {
        return memberRepository.findByMembershipDateBetweenAndChurchId(startDate, endDate, churchId);
    }
    
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
    
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
    
    public boolean existsByEmailAndChurchId(String email, Long churchId) {
        return memberRepository.existsByEmailAndChurchId(email, churchId);
    }
    
    // ========== 비즈니스 규칙 검증 메서드들 ==========
    
    /**
     * 멤버 생성 시 비즈니스 규칙 검증
     */
    private void validateMemberCreation(Member member) {
        // 교회 존재 여부 검증
        Church church = churchRepository.findById(member.getChurch().getId())
                .orElseThrow(() -> new RuntimeException("Church not found with id: " + member.getChurch().getId()));
        
        // 부서 존재 여부 검증 (부서가 지정된 경우)
        if (member.getDepartment() != null) {
            Department department = departmentRepository.findById(member.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + member.getDepartment().getId()));
            
            // 부서가 해당 교회에 속하는지 검증
            if (!department.getChurch().getId().equals(church.getId())) {
                throw new RuntimeException("Department does not belong to the specified church");
            }
        }
        
        // 이메일 중복 검증 (같은 교회 내에서)
        if (existsByEmailAndChurchId(member.getEmail(), church.getId())) {
            throw new RuntimeException("Email already exists in this church: " + member.getEmail());
        }
        
        // 필수 필드 검증
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new RuntimeException("Member name is required");
        }
        
        if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Member email is required");
        }
        
        // 이메일 형식 검증 (간단한 검증)
        if (!member.getEmail().contains("@")) {
            throw new RuntimeException("Invalid email format");
        }
        
        // 생년월일 검증
        if (member.getBirthDate() != null && member.getBirthDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Birth date cannot be in the future");
        }
        
        // 세례일 검증
        if (member.getBaptismDate() != null && member.getBirthDate() != null && 
            member.getBaptismDate().isBefore(member.getBirthDate())) {
            throw new RuntimeException("Baptism date cannot be before birth date");
        }
        
        // 입교일 검증
        if (member.getMembershipDate() != null && member.getBirthDate() != null && 
            member.getMembershipDate().isBefore(member.getBirthDate())) {
            throw new RuntimeException("Membership date cannot be before birth date");
        }
    }
    
    /**
     * 멤버 수정 시 비즈니스 규칙 검증
     */
    private void validateMemberUpdate(Member existingMember, Member memberData) {
        // 이메일 변경 시 중복 검증
        if (!existingMember.getEmail().equals(memberData.getEmail()) && 
            existsByEmailAndChurchId(memberData.getEmail(), existingMember.getChurch().getId())) {
            throw new RuntimeException("Email already exists in this church: " + memberData.getEmail());
        }
        
        // 부서 변경 시 검증
        if (memberData.getDepartment() != null && 
            !memberData.getDepartment().getId().equals(existingMember.getDepartment().getId())) {
            Department newDepartment = departmentRepository.findById(memberData.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + memberData.getDepartment().getId()));
            
            // 새 부서가 해당 교회에 속하는지 검증
            if (!newDepartment.getChurch().getId().equals(existingMember.getChurch().getId())) {
                throw new RuntimeException("Department does not belong to the member's church");
            }
        }
        
        // 역할 변경 시 검증 (목사는 한 교회에 한 명만)
        if (memberData.getRole() == MemberRole.PASTOR && 
            !existingMember.getRole().equals(MemberRole.PASTOR)) {
            List<Member> pastors = findByChurchIdAndRole(existingMember.getChurch().getId(), MemberRole.PASTOR);
            if (!pastors.isEmpty() && !pastors.get(0).getId().equals(existingMember.getId())) {
                throw new RuntimeException("Only one pastor is allowed per church");
            }
        }
    }
    
    /**
     * 멤버 삭제 시 비즈니스 규칙 검증
     */
    private void validateMemberDeletion(Member member) {
        // 목사는 삭제 불가 (예시 규칙)
        if (member.getRole() == MemberRole.PASTOR) {
            throw new RuntimeException("Cannot delete pastor. Please transfer pastor role first.");
        }
        
        // 활성 멤버는 삭제 불가 (예시 규칙)
        if (member.getStatus() == MemberStatus.ACTIVE) {
            throw new RuntimeException("Cannot delete active member. Please deactivate first.");
        }
    }
    
    /**
     * 멤버 필드 업데이트
     */
    private void updateMemberFields(Member existingMember, Member memberData) {
        if (memberData.getName() != null) {
            existingMember.setName(memberData.getName());
        }
        if (memberData.getEmail() != null) {
            existingMember.setEmail(memberData.getEmail());
        }
        if (memberData.getPhone() != null) {
            existingMember.setPhone(memberData.getPhone());
        }
        if (memberData.getAddress() != null) {
            existingMember.setAddress(memberData.getAddress());
        }
        if (memberData.getBirthDate() != null) {
            existingMember.setBirthDate(memberData.getBirthDate());
        }
        if (memberData.getGender() != null) {
            existingMember.setGender(memberData.getGender());
        }
        if (memberData.getProfileImageUrl() != null) {
            existingMember.setProfileImageUrl(memberData.getProfileImageUrl());
        }
        if (memberData.getBaptismDate() != null) {
            existingMember.setBaptismDate(memberData.getBaptismDate());
        }
        if (memberData.getMembershipDate() != null) {
            existingMember.setMembershipDate(memberData.getMembershipDate());
        }
        if (memberData.getNotes() != null) {
            existingMember.setNotes(memberData.getNotes());
        }
        if (memberData.getStatus() != null) {
            existingMember.setStatus(memberData.getStatus());
        }
        if (memberData.getRole() != null) {
            existingMember.setRole(memberData.getRole());
        }
        if (memberData.getDepartment() != null) {
            Department department = departmentRepository.findById(memberData.getDepartment().getId())
                    .orElse(null);
            existingMember.setDepartment(department);
        } else {
            existingMember.setDepartment(null);
        }
    }
} 