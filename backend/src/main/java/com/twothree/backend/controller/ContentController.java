package com.twothree.backend.controller;

import com.twothree.backend.dto.ContentDto;
import com.twothree.backend.dto.ContentSearchRequest;
import com.twothree.backend.dto.ContentIdRequest;
import com.twothree.backend.dto.ContentUpdateRequest;
import com.twothree.backend.dto.MemberIdRequest;
import com.twothree.backend.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService service;

    @PostMapping("/create")
    public ResponseEntity<ContentDto> createContent(@RequestBody ContentDto contentDto) {
        ContentDto created = service.createContent(contentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/get")
    public ResponseEntity<ContentDto> getContent(@RequestBody ContentIdRequest request) {
        return ResponseEntity.ok(service.getContent(request.getContentId()));
    }

    @PostMapping("/update")
    public ResponseEntity<ContentDto> updateContent(@RequestBody ContentUpdateRequest request) {
        return ResponseEntity.ok(service.updateContent(request.getContentId(), request.getContentDto()));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteContent(@RequestBody ContentIdRequest request) {
        service.deleteContent(request.getContentId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/list-by-member")
    public ResponseEntity<java.util.List<ContentDto>> getContentsForMember(@RequestBody MemberIdRequest request) {
        return ResponseEntity.ok(service.getContentsForMember(request.getMemberId()));
    }

    @PostMapping("/list-by-my-departments")
    public ResponseEntity<java.util.List<ContentDto>> getContentsForMyDepartments(@RequestBody MemberIdRequest request) {
        return ResponseEntity.ok(service.getContentsForMyDepartments(request.getMemberId()));
    }

    @PostMapping("/list-by-children-departments")
    public ResponseEntity<java.util.List<ContentDto>> getContentsForChildrenDepartments(@RequestBody MemberIdRequest request) {
        return ResponseEntity.ok(service.getContentsForChildrenDepartments(request.getMemberId()));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ContentDto>> searchContents(@RequestBody ContentSearchRequest req) {
        return ResponseEntity.ok(service.searchContents(req));
    }

    // ... 기존 코드 ...
} 