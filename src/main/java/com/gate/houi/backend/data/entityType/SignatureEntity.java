package com.gate.houi.backend.data.entityType;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "signature_data", indexes = {
    @Index(name = "idx_signature_student_uuid", columnList = "student_uuid")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_uuid", nullable = false)
    private UUID studentUuid;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_uuid", referencedColumnName = "student_uuid", insertable = false, updatable = false)
    private StudentEntity studentEntity;

    // 업로드된 서명 이미지 경로(혹은 URL)
    @Column(name = "signature_url", length = 500, nullable = false)
    private String signatureUrl;
}
