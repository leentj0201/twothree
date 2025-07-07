package com.twothree.backend.service;

import com.twothree.backend.dto.ContentDto;
import com.twothree.backend.entity.Content;
import com.twothree.backend.entity.ContentDepartment;
import com.twothree.backend.entity.Department;
import com.twothree.backend.entity.Member;
import com.twothree.backend.entity.Church;
import com.twothree.backend.repository.ContentDepartmentRepository;
import com.twothree.backend.repository.ContentRepository;
import com.twothree.backend.repository.DepartmentRepository;
import com.twothree.backend.repository.MemberRepository;
import com.twothree.backend.repository.ChurchRepository;
import com.twothree.backend.repository.MemberDepartmentRepository;
import com.twothree.backend.repository.MemberRelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.twothree.backend.dto.ContentSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final ContentDepartmentRepository contentDepartmentRepository;
    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;
    private final ChurchRepository churchRepository;
    private final MemberDepartmentRepository memberDepartmentRepository;
    private final MemberRelationRepository memberRelationRepository;

    @Transactional
    public ContentDto createContent(ContentDto dto) {
        // 1. Content 저장
        Member author = memberRepository.findById(dto.getAuthorId()).orElseThrow();
        Church church = churchRepository.findById(dto.getChurchId()).orElseThrow();
        Content content = new Content();
        content.setTitle(dto.getTitle());
        content.setBody(dto.getBody());
        content.setAuthor(author);
        content.setChurch(church);
        Content saved = contentRepository.save(content);

        // 2. ContentDepartment 매핑
        if (dto.getDepartmentIds() != null) {
            for (Long deptId : dto.getDepartmentIds()) {
                Department dept = departmentRepository.findById(deptId).orElseThrow();
                ContentDepartment cd = new ContentDepartment();
                cd.setContent(saved);
                cd.setDepartment(dept);
                contentDepartmentRepository.save(cd);
            }
        }

        // 3. 결과 반환
        ContentDto result = new ContentDto();
        result.setId(saved.getId());
        result.setTitle(saved.getTitle());
        result.setBody(saved.getBody());
        result.setAuthorId(saved.getAuthor().getId());
        result.setChurchId(saved.getChurch().getId());
        result.setCreatedAt(saved.getCreatedAt());
        result.setUpdatedAt(saved.getUpdatedAt());
        result.setDepartmentIds(dto.getDepartmentIds());
        return result;
    }

    public ContentDto getContent(Long id) {
        Content content = contentRepository.findById(id).orElseThrow();
        ContentDto dto = new ContentDto();
        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setBody(content.getBody());
        dto.setAuthorId(content.getAuthor().getId());
        dto.setChurchId(content.getChurch().getId());
        dto.setCreatedAt(content.getCreatedAt());
        dto.setUpdatedAt(content.getUpdatedAt());
        // 공개범위 부서
        List<Long> deptIds = contentDepartmentRepository.findByContentId(content.getId())
                .stream().map(cd -> cd.getDepartment().getId()).collect(Collectors.toList());
        dto.setDepartmentIds(deptIds);
        return dto;
    }

    @Transactional
    public ContentDto updateContent(Long id, ContentDto dto) {
        Content content = contentRepository.findById(id).orElseThrow();
        content.setTitle(dto.getTitle());
        content.setBody(dto.getBody());
        contentRepository.save(content);
        // 공개범위 부서 업데이트
        contentDepartmentRepository.deleteAll(contentDepartmentRepository.findByContentId(id));
        if (dto.getDepartmentIds() != null) {
            for (Long deptId : dto.getDepartmentIds()) {
                Department dept = departmentRepository.findById(deptId).orElseThrow();
                ContentDepartment cd = new ContentDepartment();
                cd.setContent(content);
                cd.setDepartment(dept);
                contentDepartmentRepository.save(cd);
            }
        }
        return getContent(id);
    }

    @Transactional
    public void deleteContent(Long id) {
        contentDepartmentRepository.deleteAll(contentDepartmentRepository.findByContentId(id));
        contentRepository.deleteById(id);
    }

    // 멤버별(공개범위 포함) 게시물 조회
    public List<ContentDto> getContentsForMember(Long memberId) {
        // 1. 멤버가 속한 부서
        List<Long> myDeptIds = memberDepartmentRepository.findByMemberId(memberId)
                .stream().map(md -> md.getDepartment().getId()).collect(Collectors.toList());
        // 2. 자녀가 속한 부서
        List<Long> childIds = memberRelationRepository.findByParentId(memberId)
                .stream().map(rel -> rel.getChild().getId()).collect(Collectors.toList());
        List<Long> childDeptIds = childIds.stream()
                .flatMap(cid -> memberDepartmentRepository.findByMemberId(cid).stream())
                .map(md -> md.getDepartment().getId()).collect(Collectors.toList());
        // 3. 전체 접근 가능한 부서
        List<Long> allDeptIds = myDeptIds;
        for (Long id : childDeptIds) if (!allDeptIds.contains(id)) allDeptIds.add(id);
        // 4. 해당 부서에 공개된 게시물
        List<Long> contentIds = contentDepartmentRepository.findAll().stream()
                .filter(cd -> allDeptIds.contains(cd.getDepartment().getId()))
                .map(cd -> cd.getContent().getId())
                .distinct().collect(Collectors.toList());
        return contentRepository.findAllById(contentIds).stream().map(this::toDto).collect(Collectors.toList());
    }

    // 내가 속한 부서의 게시물만 조회
    public List<ContentDto> getContentsForMyDepartments(Long memberId) {
        List<Long> myDeptIds = memberDepartmentRepository.findByMemberId(memberId)
                .stream().map(md -> md.getDepartment().getId()).collect(Collectors.toList());
        List<Long> contentIds = contentDepartmentRepository.findAll().stream()
                .filter(cd -> myDeptIds.contains(cd.getDepartment().getId()))
                .map(cd -> cd.getContent().getId())
                .distinct().collect(Collectors.toList());
        return contentRepository.findAllById(contentIds).stream().map(this::toDto).collect(Collectors.toList());
    }

    // 내 자녀가 속한 부서의 게시물만 조회
    public List<ContentDto> getContentsForChildrenDepartments(Long memberId) {
        List<Long> childIds = memberRelationRepository.findByParentId(memberId)
                .stream().map(rel -> rel.getChild().getId()).collect(Collectors.toList());
        List<Long> childDeptIds = childIds.stream()
                .flatMap(cid -> memberDepartmentRepository.findByMemberId(cid).stream())
                .map(md -> md.getDepartment().getId()).collect(Collectors.toList());
        List<Long> contentIds = contentDepartmentRepository.findAll().stream()
                .filter(cd -> childDeptIds.contains(cd.getDepartment().getId()))
                .map(cd -> cd.getContent().getId())
                .distinct().collect(Collectors.toList());
        return contentRepository.findAllById(contentIds).stream().map(this::toDto).collect(Collectors.toList());
    }

    public Page<ContentDto> searchContents(ContentSearchRequest req) {
        Pageable pageable = PageRequest.of(
                req.getPage() != null ? req.getPage() : 0,
                req.getSize() != null ? req.getSize() : 20,
                Sort.by(Sort.Direction.fromString(req.getSortDir() != null ? req.getSortDir() : "DESC"),
                        req.getSortBy() != null ? req.getSortBy() : "createdAt")
        );
        Specification<Content> spec = (root, query, cb) -> {
            Predicate p = cb.conjunction();
            if (req.getKeyword() != null && !req.getKeyword().isEmpty()) {
                Predicate title = cb.like(root.get("title"), "%" + req.getKeyword() + "%");
                Predicate body = cb.like(root.get("body"), "%" + req.getKeyword() + "%");
                p = cb.and(p, cb.or(title, body));
            }
            if (req.getAuthorId() != null) {
                p = cb.and(p, cb.equal(root.get("author").get("id"), req.getAuthorId()));
            }
            if (req.getChurchId() != null) {
                p = cb.and(p, cb.equal(root.get("church").get("id"), req.getChurchId()));
            }
            if (req.getDepartmentId() != null) {
                Join<Content, ContentDepartment> join = root.join("id"); // 실제로는 ContentDepartmentRepository로 별도 쿼리 권장
                p = cb.and(p, cb.equal(join.get("department").get("id"), req.getDepartmentId()));
            }
            if (req.getFromDate() != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("createdAt"), req.getFromDate().atStartOfDay()));
            }
            if (req.getToDate() != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("createdAt"), req.getToDate().atTime(23,59,59)));
            }
            return p;
        };
        return contentRepository.findAll(spec, pageable).map(this::toDto);
    }

    private ContentDto toDto(Content content) {
        ContentDto dto = new ContentDto();
        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setBody(content.getBody());
        dto.setAuthorId(content.getAuthor().getId());
        dto.setChurchId(content.getChurch().getId());
        dto.setCreatedAt(content.getCreatedAt());
        dto.setUpdatedAt(content.getUpdatedAt());
        List<Long> deptIds = contentDepartmentRepository.findByContentId(content.getId())
                .stream().map(cd -> cd.getDepartment().getId()).collect(Collectors.toList());
        dto.setDepartmentIds(deptIds);
        return dto;
    }
} 