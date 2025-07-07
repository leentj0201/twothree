package com.twothree.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import com.twothree.backend.enums.ChurchStatus;

@Entity
@Table(name = "churches")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Church extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private String address;
    
    @Column
    private String phone;
    
    @Column
    private String email;
    
    @Column
    private String website;
    
    @Column
    private String pastorName;
    
    @Column
    private String pastorPhone;
    
    @Column
    private String pastorEmail;
    
    @Column
    private String logoUrl;
    
    @Column
    private String bannerUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChurchStatus status = ChurchStatus.ACTIVE;
    
    @OneToMany(mappedBy = "church", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Department> departments;
    
    @OneToMany(mappedBy = "church", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Member> members;
} 