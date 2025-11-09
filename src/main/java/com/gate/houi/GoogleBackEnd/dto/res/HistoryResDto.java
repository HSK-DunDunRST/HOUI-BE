package com.gate.houi.GoogleBackEnd.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryResDto {

    private Long id;

    private String symptomsContent;

    private String prescriptionContent;

    private String campusType;

    private String registerAt;
}
