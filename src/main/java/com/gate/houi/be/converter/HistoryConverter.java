package com.gate.houi.be.converter;

import com.gate.houi.be.dto.res.HistoryResDto;
import com.gate.houi.be.entity.HistoryEntity;

public class HistoryConverter {

    public static HistoryResDto toHistory(HistoryEntity historyEntity) {
        return HistoryResDto.builder()
                .id(historyEntity.getId())
                .symptomsContent(historyEntity.getSymptomsContent())
                .prescriptionContent(historyEntity.getPrescriptionContent())
                .registerAt(historyEntity.getCreatedAt().toString())
                .build();
    }
}
