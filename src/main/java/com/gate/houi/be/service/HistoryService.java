package com.gate.houi.be.service;

import com.gate.houi.be.dto.res.HistoryResDto;

import java.util.List;

public interface HistoryService {
    List<HistoryResDto> getReceptionHistory(Long userId);
}
