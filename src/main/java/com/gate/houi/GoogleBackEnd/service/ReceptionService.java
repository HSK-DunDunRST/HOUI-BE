package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.dto.req.ReceptionReqDto;
import com.gate.houi.GoogleBackEnd.dto.res.HistoryResDto;
import com.gate.houi.GoogleBackEnd.dto.res.MainResDto;

import java.util.List;

public interface ReceptionService {
    MainResDto.WaitInformation RegisterReception(ReceptionReqDto receptionReqDto, Long userId);
    MainResDto.WaitInformation waitCount();
    List<HistoryResDto> getReceptionHistory(Long userId);
}
