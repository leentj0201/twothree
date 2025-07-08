package com.twothree.backend.repository;

import com.twothree.backend.entity.Department;
import com.twothree.backend.enums.DepartmentStatus;
import com.twothree.backend.enums.DepartmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    List<Department> findByChurchId(Long churchId);
    
    List<Department> findByChurchIdAndStatus(Long churchId, DepartmentStatus status);
    
    List<Department> findByCategoryAndChurchId(DepartmentCategory category, Long churchId);
    
    List<Department> findByCategoryAndChurchIdAndStatus(DepartmentCategory category, Long churchId, DepartmentStatus status);
    
    Optional<Department> findByNameAndChurchId(String name, Long churchId);
    
    @Query("SELECT d FROM Department d WHERE d.church.id = :churchId AND (d.name LIKE %:keyword% OR d.description LIKE %:keyword%)")
    List<Department> searchByKeywordAndChurchId(@Param("keyword") String keyword, @Param("churchId") Long churchId);
    
    boolean existsByNameAndChurchId(String name, Long churchId);
} 