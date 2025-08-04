package com.gate.houi.backend.dto.main;

import com.gate.houi.backend.dto.notice.NoticeResponseDTO;
import com.gate.houi.backend.dto.reception.ReceptionStatusDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeResponseDTO {
    private NoticeResponseDTO notice;
    private ReceptionStatusDTO reception;
}
