package com.gate.houi.backend.dto.reception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceptionStatusDTO {
    private Integer waitingPeople;
}
