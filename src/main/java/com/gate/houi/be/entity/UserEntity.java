package com.gate.houi.be.entity;

import com.gate.houi.be.entity.common.BaseTimeEntity;
import com.gate.houi.be.entity.enums.Provider;
import com.gate.houi.be.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "student_data")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UUID v4 (랜덤 UUID) 사용 - Hibernate 최신 방식
    @UuidGenerator
    @Column(name = "user_uuid", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID userUuid;

    @Column(name = "student_id", nullable = false, length = 9)
    private String studentId;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider oauth_provider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
