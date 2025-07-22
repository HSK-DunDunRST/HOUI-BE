package com.gate.houi.backend.data.entityType;


import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import com.gate.houi.backend.data.common.BaseTimeEntity;

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

    @Column(name = "account_uuid", nullable = false, unique = true, updatable = false)
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

    @PrePersist
    protected void onCreate() {
        if (this.accountUuid == null) {
            this.accountUuid = UUID.randomUUID();
        }
    }

    public enum Provider {
        google, kakao
    }
}