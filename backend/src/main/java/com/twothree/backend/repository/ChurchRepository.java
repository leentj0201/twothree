package com.twothree.backend.repository;

import com.twothree.backend.entity.Church;
import com.twothree.backend.enums.ChurchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChurchRepository extends JpaRepository<Church, Long> {
    
    Optional<Church> findByName(String name);
    
    Optional<Church> findByEmail(String email);
    
    List<Church> findByStatus(ChurchStatus status);
    
    @Query("SELECT c FROM Church c WHERE c.name LIKE %:keyword% OR c.description LIKE %:keyword%")
    List<Church> searchByKeyword(@Param("keyword") String keyword);
    
    boolean existsByName(String name);
    
    boolean existsByEmail(String email);
    
    long deleteByStatusAndUpdatedAtBefore(ChurchStatus status, LocalDateTime cutoffDate);
} 