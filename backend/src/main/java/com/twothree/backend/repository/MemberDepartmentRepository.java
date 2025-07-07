package com.twothree.backend.repository;

import com.twothree.backend.entity.MemberDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberDepartmentRepository extends JpaRepository<MemberDepartment, Long> {
    List<MemberDepartment> findByMemberId(Long memberId);
    List<MemberDepartment> findByDepartmentId(Long departmentId);
} 