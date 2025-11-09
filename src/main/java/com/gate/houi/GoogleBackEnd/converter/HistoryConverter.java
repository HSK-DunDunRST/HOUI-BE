package com.gate.houi.GoogleBackEnd.converter;

import com.gate.houi.GoogleBackEnd.dto.res.HistoryResDto;
import com.gate.houi.GoogleBackEnd.entity.ReceptionEntity;

public class HistoryConverter {

    public static HistoryResDto toHistory(ReceptionEntity receptionEntity) {
        return HistoryResDto.builder()
                .id(receptionEntity.getId())
                .symptomsContent(receptionEntity.getSymptomsContent())
                .prescriptionContent(receptionEntity.getPrescriptionContent())
                .campusType(receptionEntity.getCampusType().toString())
                .registerAt(receptionEntity.getCreatedAt().toString())
                .build();
    }
}
