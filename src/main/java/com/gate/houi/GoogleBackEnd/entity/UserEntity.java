package com.gate.houi.GoogleBackEnd.entity;

import com.gate.houi.GoogleBackEnd.entity.common.BaseTimeEntity;
import com.gate.houi.GoogleBackEnd.entity.enums.Campus;
import com.gate.houi.GoogleBackEnd.entity.enums.Provider;
import com.gate.houi.GoogleBackEnd.entity.enums.UserRole;
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

    private String userPwd; // 관리자만 사용

    @Column(name = "user_email", nullable = true, length = 50)
    private String userEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider oauthProvider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "user_campus")
    @Enumerated(EnumType.STRING)
    private Campus userCampus;
}