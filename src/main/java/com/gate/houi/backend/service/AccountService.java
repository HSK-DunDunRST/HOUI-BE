package com.gate.houi.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.backend.data.entityType.AccountEntity;
import com.gate.houi.backend.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public AccountProfileResponseDTO getAccountProfile(String oauthId) {
        AccountEntity userEntity = accountRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException());

        return convertToDto(userEntity);
    }

    @Transactional(readOnly = true)
    public AccountProfileResponseDTO getAccountProfileByEmail(String email) {
        AccountEntity userEntity = accountRepository.findByAccountEmail(email)
                .orElseThrow(() -> new UserNotFoundException());

        return convertToDto(userEntity);
    }

    @Transactional(readOnly = true)
    public AccountProfileResponseDTO getDetailedAccountProfile(String oauthId) {
        AccountEntity userEntity = accountRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException());

        return AccountProfileResponseDTO.builder()
                .accountUuid(userEntity.getAccountUuid())
                .studentId(userEntity.getStudentId())
                .accountName(userEntity.getAccountName())
                .oauthProvider(userEntity.getOauthProvider())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }

    private AccountProfileResponseDTO convertToDto(AccountEntity accountEntity) {
        return AccountProfileResponseDTO.builder()
                .accountUuid(accountEntity.getAccountUuid())
                .studentId(accountEntity.getStudentId())
                .accountName(accountEntity.getAccountName())
                .oauthProvider(accountEntity.getOauthProvider())
                .createdAt(accountEntity.getCreatedAt())
                .build();
    }
}
