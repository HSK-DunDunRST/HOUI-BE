package com.gate.houi.hoseoHoui.domain.dtoSet;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class  ReceptionResponseDTO {
    private Long id;
    private String symptomsContent;
    private String receptionStatus;
    private String receptionCreatedAt;
}
