package com.gate.houi.backend.dto.history;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UseHistoryResponseDTO {
    private Long id;
    private String symptomsContent;
    private String prescriptionContent;
    private String receptionCreatedAt;
    private String studentId;
    private String studentName;
}
