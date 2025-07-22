package com.gate.houi.backend.dto.account;

import java.time.LocalDateTime;
import java.util.UUID;

import com.gate.houi.backend.data.entityType.AccountEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountProfileResponseDTO {
    
    private UUID accountUuid;
    private String studentId;;
    private String accountName;
    private String accountEmail;
    private AccountEntity.Provider oauthProvider;
    private LocalDateTime createdAt;
}
