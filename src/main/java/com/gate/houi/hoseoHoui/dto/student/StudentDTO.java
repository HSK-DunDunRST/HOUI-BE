package com.gate.houi.hoseoHoui.dto.student;

import com.gate.houi.hoseoHoui.domain.entity.StudentEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Long id;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private StudentEntity.Provider oauthProvider;
}

