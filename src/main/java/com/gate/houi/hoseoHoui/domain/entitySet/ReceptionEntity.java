package com.gate.houi.hoseoHoui.domain.entitySet;

import com.gate.houi.hoseoHoui.domain.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    @Column(name = "symptoms_content", nullable = false)
    private String symptomsContent;

    @Column(name = "reception_status", nullable = false)
    private String receptionStatus;
    
    @ManyToOne(targetEntity = StudentEntity.class ,fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity studentEntity;

}
