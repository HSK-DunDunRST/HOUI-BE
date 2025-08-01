package com.gate.houi.backend.data.entityType;


import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.gate.houi.backend.data.common.BaseTimeEntity;
import com.gate.houi.backend.data.enumType.UserRole;

@Entity
@Table(name = "student_data", indexes = {
    @Index(name = "idx_student_uuid", columnList = "student_uuid"),
    @Index(name = "idx_oauth_id", columnList = "oauth_id"),
    @Index(name = "idx_student_id", columnList = "student_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UUID v4 (랜덤 UUID) 사용 - Hibernate 최신 방식
    @UuidGenerator
    @Column(name = "student_uuid", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID studentUuid;

    @Column(name = "oauth_id", nullable = false, unique = true, length = 500)
    private String oauthId;
    
    @Column(name = "student_id", nullable = false, length = 9)
    private String studentId;

    @Column(name = "student_name" ,nullable = false, length = 50)
    private String studentName;

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
        google
    }
}