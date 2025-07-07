package com.twothree.backend.repository;

import com.twothree.backend.entity.Member;
import com.twothree.backend.enums.MemberStatus;
import com.twothree.backend.enums.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    List<Member> findByChurchId(Long churchId);
    
    Page<Member> findByChurchId(Long churchId, Pageable pageable);
    
    List<Member> findByChurchIdAndStatus(Long churchId, MemberStatus status);
    
    List<Member> findByDepartmentId(Long departmentId);
    
    List<Member> findByChurchIdAndDepartmentId(Long churchId, Long departmentId);
    
    List<Member> findByChurchIdAndRole(Long churchId, MemberRole role);
    
    Optional<Member> findByEmail(String email);
    
    Optional<Member> findByEmailAndChurchId(String email, Long churchId);
    
    @Query("SELECT m FROM Member m WHERE m.church.id = :churchId AND (m.name LIKE %:keyword% OR m.email LIKE %:keyword% OR m.phone LIKE %:keyword%)")
    List<Member> searchByKeywordAndChurchId(@Param("keyword") String keyword, @Param("churchId") Long churchId);
    
    @Query("SELECT m FROM Member m WHERE m.church.id = :churchId AND m.birthDate BETWEEN :startDate AND :endDate")
    List<Member> findByBirthDateBetweenAndChurchId(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("churchId") Long churchId);
    
    @Query("SELECT m FROM Member m WHERE m.church.id = :churchId AND m.membershipDate BETWEEN :startDate AND :endDate")
    List<Member> findByMembershipDateBetweenAndChurchId(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("churchId") Long churchId);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndChurchId(String email, Long churchId);
} 