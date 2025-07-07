package com.twothree.backend.dto;

import com.twothree.backend.enums.MemberRole;
import lombok.Data;

@Data
public class ChurchIdRoleRequest {
    private Long churchId;
    private MemberRole role;
} 