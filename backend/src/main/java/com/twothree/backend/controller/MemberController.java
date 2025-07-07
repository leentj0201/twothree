package com.twothree.backend.controller;

import com.twothree.backend.dto.MemberDto;
import com.twothree.backend.enums.MemberRole;
import com.twothree.backend.enums.MemberStatus;
import com.twothree.backend.entity.Member;
import com.twothree.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import com.twothree.backend.dto.MemberListRequest;
import com.twothree.backend.dto.ChurchIdRequest;
import com.twothree.backend.dto.ChurchIdPageRequest;
import com.twothree.backend.dto.ChurchIdStatusRequest;
import com.twothree.backend.dto.DepartmentIdRequest;
import com.twothree.backend.dto.ChurchDepartmentIdRequest;
import com.twothree.backend.dto.ChurchIdRoleRequest;
import com.twothree.backend.dto.MemberIdRequest;
import com.twothree.backend.dto.MemberEmailRequest;
import com.twothree.backend.dto.ChurchIdKeywordRequest;
import com.twothree.backend.dto.ChurchIdDateRangeRequest;
import com.twothree.backend.dto.MemberUpdateRequest;
import com.twothree.backend.dto.MemberEmailChurchIdRequest;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MemberController {
    
    private final MemberService memberService;
    
    @PostMapping("/list")
    public ResponseEntity<List<MemberDto>> getAllMembers(@RequestBody(required = false) MemberListRequest request) {
        List<MemberDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/list-by-church")
    public ResponseEntity<List<MemberDto>> getMembersByChurchId(@RequestBody ChurchIdRequest request) {
        List<MemberDto> members = memberService.getMembersByChurchId(request.getChurchId());
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/page-by-church")
    public ResponseEntity<Page<MemberDto>> getMembersByChurchIdPage(@RequestBody ChurchIdPageRequest request) {
        Page<MemberDto> members = memberService.getMembersByChurchId(request.getChurchId(), request.getPageable());
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/list-by-church-status")
    public ResponseEntity<List<MemberDto>> getMembersByChurchIdAndStatus(@RequestBody ChurchIdStatusRequest request) {
        List<MemberDto> members = memberService.getMembersByChurchIdAndStatus(request.getChurchId(), request.getStatus());
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/list-by-department")
    public ResponseEntity<List<MemberDto>> getMembersByDepartmentId(@RequestBody DepartmentIdRequest request) {
        List<MemberDto> members = memberService.getMembersByDepartmentId(request.getDepartmentId());
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/list-by-church-department")
    public ResponseEntity<List<MemberDto>> getMembersByChurchIdAndDepartmentId(@RequestBody ChurchDepartmentIdRequest request) {
        List<MemberDto> members = memberService.getMembersByChurchIdAndDepartmentId(request.getChurchId(), request.getDepartmentId());
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/list-by-church-role")
    public ResponseEntity<List<MemberDto>> getMembersByChurchIdAndRole(@RequestBody ChurchIdRoleRequest request) {
        List<MemberDto> members = memberService.getMembersByChurchIdAndRole(request.getChurchId(), request.getRole());
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/get")
    public ResponseEntity<MemberDto> getMemberById(@RequestBody MemberIdRequest request) {
        return memberService.getMemberById(request.getMemberId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/get-by-email")
    public ResponseEntity<MemberDto> getMemberByEmail(@RequestBody MemberEmailRequest request) {
        return memberService.getMemberByEmail(request.getEmail())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/search-by-church")
    public ResponseEntity<List<MemberDto>> searchMembersByChurchId(@RequestBody ChurchIdKeywordRequest request) {
        List<MemberDto> members = memberService.searchMembersByChurchId(request.getKeyword(), request.getChurchId());
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/birth-date-range")
    public ResponseEntity<List<MemberDto>> getMembersByBirthDateRange(@RequestBody ChurchIdDateRangeRequest request) {
        List<MemberDto> members = memberService.getMembersByBirthDateRange(request.getChurchId(), request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/membership-date-range")
    public ResponseEntity<List<MemberDto>> getMembersByMembershipDateRange(@RequestBody ChurchIdDateRangeRequest request) {
        List<MemberDto> members = memberService.getMembersByMembershipDateRange(request.getChurchId(), request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/create")
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto memberDto) {
        if (memberService.existsByEmailAndChurchId(memberDto.getEmail(), memberDto.getChurchId())) {
            return ResponseEntity.badRequest().build();
        }
        
        MemberDto createdMember = memberService.createMember(memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }
    
    @PostMapping("/update")
    public ResponseEntity<MemberDto> updateMember(@RequestBody MemberUpdateRequest request) {
        return memberService.updateMember(request.getMemberId(), request.getMemberDto())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteMember(@RequestBody MemberIdRequest request) {
        boolean deleted = memberService.deleteMember(request.getMemberId());
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkMemberEmailExists(@RequestBody MemberEmailRequest request) {
        boolean exists = memberService.existsByEmail(request.getEmail());
        return ResponseEntity.ok(exists);
    }
    
    @PostMapping("/check-email-in-church")
    public ResponseEntity<Boolean> checkMemberEmailExistsInChurch(@RequestBody MemberEmailChurchIdRequest request) {
        boolean exists = memberService.existsByEmailAndChurchId(request.getEmail(), request.getChurchId());
        return ResponseEntity.ok(exists);
    }
} 