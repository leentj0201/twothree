package com.twothree.backend.dto;

import com.twothree.backend.enums.MemberStatus;
import lombok.Data;

@Data
public class ChurchIdStatusRequest {
    private Long churchId;
    private MemberStatus status;
} 