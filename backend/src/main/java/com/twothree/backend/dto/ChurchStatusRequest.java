package com.twothree.backend.dto;

import com.twothree.backend.enums.ChurchStatus;
import lombok.Data;

@Data
public class ChurchStatusRequest {
    private ChurchStatus status;
} 