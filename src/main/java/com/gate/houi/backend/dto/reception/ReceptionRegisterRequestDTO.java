package com.gate.houi.backend.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionRegisterRequestDTO {
    private String symptomsContent;
}
