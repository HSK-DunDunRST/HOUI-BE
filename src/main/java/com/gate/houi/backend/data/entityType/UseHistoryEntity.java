package com.gate.houi.backend.data.entityType;


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

import java.util.UUID;

import com.gate.houi.backend.data.common.BaseTimeEntity;

@Table(name = "use_history_data")
@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UseHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symptoms_content", nullable = false)
    private String symptomsContent;
    
    @Column(name = "prescription_content", nullable = false)
    private String prescriptionContent;

    @Column(name = "student_uuid", nullable = false)
    private UUID studentUuid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_uuid", referencedColumnName = "account_uuid", insertable = false, updatable = false)
    private AccountEntity account;

}
