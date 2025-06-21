package com.gate.houi.hoseoHoui.dto.history;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UseHistoryResponseDTO {
    private Long id;
    private String symptomsContent;
    private String prescriptionContent;
    private String receptionCreatedAt;
}
