package com.gate.houi.backend.data.entityType;

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

import java.util.UUID;

import com.gate.houi.backend.data.common.BaseTimeEntity;
import com.gate.houi.backend.data.enumType.CampusType;
import com.gate.houi.backend.data.enumType.ReceptionType;

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
    
    @Column(name = "student_uuid", nullable = false)
    private UUID studentUuid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_uuid", referencedColumnName = "account_uuid", insertable = false, updatable = false)
    private AccountEntity account;
    
    @Column(name = "symptoms_content", nullable = false)
    private String symptomsContent;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "campus_type", nullable = false)
    private CampusType campusType;
    
    @Column(name = "prescription_content")
    private String prescriptionContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "reception_type", nullable = false)
    private ReceptionType receptionType;

}