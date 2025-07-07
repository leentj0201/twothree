package com.twothree.backend.repository;

import com.twothree.backend.entity.MemberRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberRelationRepository extends JpaRepository<MemberRelation, Long> {
    List<MemberRelation> findByParentId(Long parentId);
    List<MemberRelation> findByChildId(Long childId);
} 