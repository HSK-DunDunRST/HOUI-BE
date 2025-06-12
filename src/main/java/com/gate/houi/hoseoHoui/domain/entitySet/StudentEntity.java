package com.gate.houi.hoseoHoui.domain.entitySet;

import java.util.ArrayList;
import java.util.List;

import com.gate.houi.hoseoHoui.domain.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "student_data")
@Data
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private String student_id;
    
    @Column(name = "student_name", nullable = false)
    private String student_name;

    @Builder.Default
    @OneToMany(mappedBy = "studentEntity", cascade = CascadeType.ALL)
    private List<ReceptionEntity> receptionList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "studentEntity", cascade = CascadeType.ALL)
    private List<UseHistoryEntity> useHistoryList = new ArrayList<>();

}
