package com.twothree.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ChurchIdDateRangeRequest {
    private Long churchId;
    private LocalDate startDate;
    private LocalDate endDate;
} 