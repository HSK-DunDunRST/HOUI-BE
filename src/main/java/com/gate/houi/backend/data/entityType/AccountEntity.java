package com.gate.houi.backend.data.entityType;


import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.gate.houi.backend.data.common.BaseTimeEntity;
import com.gate.houi.backend.data.enumType.UserRole;

@Entity
@Table(name = "account_data")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UUID v4 (랜덤 UUID) 사용 - Hibernate 최신 방식
    @UuidGenerator
    @Column(name = "account_uuid", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID accountUuid;

    @Column(name = "oauth_id", nullable = false, unique = true, columnDefinition = "TEXT")
    private String oauthId;
    
    @Column(name = "student_id", nullable = false, length = 9)
    private String studentId;

    @Column(name = "account_name" ,nullable = false, length = 50)
    private String accountName;

    @Column(name = "account_email" ,nullable = false, length = 100)
    private String accountEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider oauthProvider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole role = UserRole.USER;

    @PrePersist
    protected void onCreate() {
        // 엔티티 저장 전 기본값 설정
        if (this.role == null) {
            this.role = UserRole.USER;
        }
    }

    public enum Provider {
        google, kakao
    }
}