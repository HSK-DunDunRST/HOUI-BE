package com.gate.houi.hoseoHoui.domain.entity;

import com.gate.houi.hoseoHoui.domain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_data")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 9)
    private String studentId;

    @Column(nullable = false, length = 50)
    private String stduentName;

    @Column(nullable = false, length = 100)
    private String studentEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider oauthProvider;

    @Column(name = "oauth_id", nullable = false, unique = true)
    private String oauthId;

    public enum Provider {
        google, kakao
    }
}