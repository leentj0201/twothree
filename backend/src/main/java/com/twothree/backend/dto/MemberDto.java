package com.twothree.backend.dto;

import com.twothree.backend.enums.Gender;
import com.twothree.backend.enums.MemberStatus;
import com.twothree.backend.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private Gender gender;
    private MemberStatus status;
    private MemberRole role;
    private String profileImageUrl;
    private LocalDate baptismDate;
    private LocalDate membershipDate;
    private String notes;
    private Long churchId;
    private Long departmentId;
    private String departmentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    public static MemberDto fromEntity(com.twothree.backend.entity.Member member) {
        MemberDto dto = MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .birthDate(member.getBirthDate())
                .gender(member.getGender())
                .status(member.getStatus())
                .role(member.getRole())
                .profileImageUrl(member.getProfileImageUrl())
                .baptismDate(member.getBaptismDate())
                .membershipDate(member.getMembershipDate())
                .notes(member.getNotes())
                .churchId(member.getChurch().getId())
                .departmentId(member.getDepartment() != null ? member.getDepartment().getId() : null)
                .departmentName(member.getDepartment() != null ? member.getDepartment().getName() : null)
                .build();
        
        dto.setCreatedAt(member.getCreatedAt());
        dto.setUpdatedAt(member.getUpdatedAt());
        dto.setCreatedBy(member.getCreatedBy());
        dto.setUpdatedBy(member.getUpdatedBy());
        
        return dto;
    }
} 