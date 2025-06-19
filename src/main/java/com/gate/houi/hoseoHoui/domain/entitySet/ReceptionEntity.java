package com.gate.houi.hoseoHoui.domain.entitySet;

import com.gate.houi.hoseoHoui.domain.common.BaseTimeEntity;
import com.gate.houi.hoseoHoui.domain.enumSet.ReceptionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    /**
     * 진료 접수 상태
     * WAITING: 대기중
     * COMPLETED: 완료
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "reception_status", nullable = false)
    private ReceptionType receptionStatus;

    @Column(name = "student_id", nullable = false)
    private String studentId;
    
    @Column(name = "student_name", nullable = false)
    private String studentName;
}