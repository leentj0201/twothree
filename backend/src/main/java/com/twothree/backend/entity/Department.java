package com.twothree.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import com.twothree.backend.enums.DepartmentStatus;
import com.twothree.backend.enums.DepartmentCategory;

@Entity
@Table(name = "departments")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column
    private String color;
    
    @Column
    private String icon;
    
    // 상위 부서 대신 카테고리 필드를 추가합니다. (예: 교육위원회, 전도위원회, 목장 등)
    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private DepartmentCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    private Church church;
    
    
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Member> members;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepartmentStatus status = DepartmentStatus.ACTIVE;
} 