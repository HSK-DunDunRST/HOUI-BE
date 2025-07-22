package com.gate.houi.backend.dto.reception;

import java.time.LocalDateTime;

import com.gate.houi.backend.data.enumType.CampusType;
import com.gate.houi.backend.data.enumType.ReceptionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceptionHistoryResponseDTO {
    private Long id;
    private String symptomsContent;
    private CampusType campusType;
    private String prescriptionContent;
    private ReceptionType receptionType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
