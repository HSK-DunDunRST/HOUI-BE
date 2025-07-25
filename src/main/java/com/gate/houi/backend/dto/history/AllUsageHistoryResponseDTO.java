package com.gate.houi.backend.dto.history;

import java.time.LocalDateTime;

import com.gate.houi.backend.data.enumType.CampusType;
import com.gate.houi.backend.data.enumType.ReceptionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllUsageHistoryResponseDTO {
    private Long id;
    private String studentId;
    private String studentName;
    private String symptomsContent;
    private String prescriptionContent;
    private CampusType campusType;
    private ReceptionType receptionType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
