package com.gate.houi.be.entity;

import com.gate.houi.be.entity.common.BaseTimeEntity;
import com.gate.houi.be.entity.enums.Provider;
import com.gate.houi.be.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
@Entity
@Table(name = "users")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(name = "user_uuid", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID userUuid;

    @Column(name = "student_id", unique = true, length = 9)
    private String studentId; // 학생만 사용

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    private String loginPwd; // 관리자만 사용

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider oauthProvider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
}