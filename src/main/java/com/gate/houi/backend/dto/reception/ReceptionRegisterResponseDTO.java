package com.gate.houi.backend.dto.reception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceptionRegisterResponseDTO {
    private Long id;
    private Integer waitingPeople;
}
