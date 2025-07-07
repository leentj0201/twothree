package com.twothree.backend.dto;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private Long memberId;
    private MemberDto memberDto;
} 