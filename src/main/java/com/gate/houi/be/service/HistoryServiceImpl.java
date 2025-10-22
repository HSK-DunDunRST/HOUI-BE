package com.gate.houi.be.service;

import com.gate.houi.be.apiPayload.code.exception.BaseException;
import com.gate.houi.be.apiPayload.code.status.ErrorType;
import com.gate.houi.be.converter.HistoryConverter;
import com.gate.houi.be.dto.res.HistoryResDto;
import com.gate.houi.be.entity.HistoryEntity;
import com.gate.houi.be.entity.UserEntity;
import com.gate.houi.be.repository.HistoryRepository;
import com.gate.houi.be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HistoryResDto> getReceptionHistory(Long userId) {

        UserEntity searchByUserId = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        List<HistoryEntity> convertListHistory = historyRepository.findAllByUserEntityOrderByCreatedAtDesc(searchByUserId);

        return convertListHistory.stream()
                .map(HistoryConverter::toHistory)
                .toList();
    }
}
