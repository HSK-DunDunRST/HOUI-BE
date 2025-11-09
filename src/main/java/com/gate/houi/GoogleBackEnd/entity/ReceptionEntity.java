package com.gate.houi.GoogleBackEnd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.gate.houi.GoogleBackEnd.entity.common.BaseTimeEntity;
import com.gate.houi.GoogleBackEnd.entity.enums.Campus;
import com.gate.houi.GoogleBackEnd.entity.enums.Reception;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "reception_data")
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceptionEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // StudentEntity의 UUID v4를 참조
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "symptoms_content", nullable = false)
    private String symptomsContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "campus_type", nullable = false)
    private Campus campusType;

    @Column(name = "prescription_content")
    private String prescriptionContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "reception_type", nullable = false)
    private Reception receptionType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", referencedColumnName = "user_uuid", insertable = false, updatable = false)
    private UserEntity userEntity;
}
