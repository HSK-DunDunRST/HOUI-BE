package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.apiPayload.code.exception.BaseException;
import com.gate.houi.GoogleBackEnd.apiPayload.code.status.ErrorType;
import com.gate.houi.GoogleBackEnd.converter.HistoryConverter;
import com.gate.houi.GoogleBackEnd.converter.ReceptionConverter;
import com.gate.houi.GoogleBackEnd.dto.req.ReceptionReqDto;
import com.gate.houi.GoogleBackEnd.dto.res.HistoryResDto;
import com.gate.houi.GoogleBackEnd.dto.res.MainResDto;
import com.gate.houi.GoogleBackEnd.entity.ReceptionEntity;
import com.gate.houi.GoogleBackEnd.entity.UserEntity;
import com.gate.houi.GoogleBackEnd.entity.enums.Campus;
import com.gate.houi.GoogleBackEnd.entity.enums.Reception;
import com.gate.houi.GoogleBackEnd.repository.ReceptionRepository;
import com.gate.houi.GoogleBackEnd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReceptionServiceImpl implements ReceptionService {

    private final UserRepository userRepository;
    private final ReceptionRepository receptionRepository;

    @Override
    @Transactional
    public MainResDto.WaitInformation RegisterReception(ReceptionReqDto receptionReqDto, Long userId) {
        // 필수 데이터(진료 증상)가 전달되지 않았으면 예외 발생
        if (receptionReqDto.getSymptomsContent().isEmpty()) {
            throw new BaseException(ErrorType.RESOURCE_NOT_FOUND);
        }

        Optional<UserEntity> userEntity = userRepository.findById(userId);

        ReceptionEntity receptionEntity = userEntity
                .map(student -> ReceptionEntity.builder()
                        .symptomsContent(receptionReqDto.getSymptomsContent())
                        .campusType(Campus.ASAN) // 아산캠퍼스로 고정
                        .receptionType(Reception.WAITING) // 기본 상태는 대기
                        .userUuid(student.getUserUuid()) // studentUuid 사용
                        .build())
                .orElseThrow(() -> new BaseException(ErrorType._INTERNAL_SERVER_ERROR));

        receptionRepository.save(receptionEntity);

        return ReceptionConverter.toWait(waitCount().getWaitCount());
    }

    @Override
    @Transactional(readOnly = true)
    public MainResDto.WaitInformation waitCount() {
        return ReceptionConverter.toWait(receptionRepository.countByReceptionType(Reception.WAITING));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryResDto> getReceptionHistory(Long userId) {

        UserEntity searchByUserId = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        List<ReceptionEntity> convertListHistory = receptionRepository.findAllByUserEntityAndReceptionTypeOrderByCreatedAtDesc(searchByUserId, Reception.SUCCESSFUL);

        return convertListHistory.stream()
                .map(HistoryConverter::toHistory)
                .toList();
    }
}
