package com.twothree.backend.repository;

import com.twothree.backend.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContentRepository extends JpaRepository<Content, Long>, JpaSpecificationExecutor<Content> {
} 