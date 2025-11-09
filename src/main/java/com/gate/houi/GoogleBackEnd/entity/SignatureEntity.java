package com.gate.houi.GoogleBackEnd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gate.houi.GoogleBackEnd.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "signature_data")
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignatureEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // StudentEntity의 UUID v4를 참조
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    // 업로드된 이미지 경로(혹은 URL)
    @Column(name = "signature_url", length = 500, nullable = false)
    private String signatureUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", referencedColumnName = "user_uuid", insertable = false, updatable = false)
    private UserEntity userEntity;
}
