package com.gate.houi.backend.dto.student;

import java.time.LocalDateTime;
import java.util.UUID;

import com.gate.houi.backend.data.entityType.StudentEntity;

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
public class StudentProfileResponseDTO {
    
    private UUID studentUuid;
    private String studentId;
    private String studentName;
    private StudentEntity.Provider oauthProvider;
    private LocalDateTime createdAt;
}
