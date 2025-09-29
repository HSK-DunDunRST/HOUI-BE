package com.gate.houi.be.service;

import com.gate.houi.be.dto.req.ReceptionReqDto;
import com.gate.houi.be.dto.res.MainResDto;

public interface ReceptionService {
    void RegisterReception(ReceptionReqDto receptionReqDto, Long userId);
    MainResDto.WaitInformation waitCount();
}
