package com.twothree.backend.repository;

import com.twothree.backend.entity.ContentDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContentDepartmentRepository extends JpaRepository<ContentDepartment, Long> {
    List<ContentDepartment> findByContentId(Long contentId);
    List<ContentDepartment> findByDepartmentId(Long departmentId);
} 