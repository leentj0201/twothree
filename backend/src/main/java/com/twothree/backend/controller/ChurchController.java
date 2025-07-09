package com.twothree.backend.controller;

import com.twothree.backend.dto.ChurchDto;
import com.twothree.backend.dto.ChurchEmailRequest;
import com.twothree.backend.dto.ChurchIdRequest;
import com.twothree.backend.dto.ChurchListRequest;
import com.twothree.backend.dto.ChurchNameRequest;
import com.twothree.backend.dto.ChurchSearchRequest;
import com.twothree.backend.dto.ChurchStatusRequest;
import com.twothree.backend.dto.ChurchUpdateRequest;
import com.twothree.backend.service.ChurchExcelService;
import com.twothree.backend.service.ChurchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/churches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "교회 관리", description = "교회 정보 관리 API")
public class ChurchController {

    private final ChurchService churchService;
    private final ChurchExcelService churchExcelService;

    @PostMapping("/list")
    @Operation(
        summary = "교회 목록 조회",
        description = "등록된 모든 교회의 목록을 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "교회 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChurchDto.class)
            )
        ),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<List<ChurchDto>> getAllChurches(
        @Parameter(description = "교회 목록 요청 파라미터 (선택사항)")
        @RequestBody(required = false) ChurchListRequest request
    ) {
        List<ChurchDto> churches = churchService.getAllChurches();
        return ResponseEntity.ok(churches);
    }

    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> downloadChurchExcel() throws IOException {
        List<ChurchDto> churches = churchService.getAllChurches();
        ByteArrayOutputStream excelStream = churchExcelService.generateChurchExcel(
            churches.stream()
                .map(churchDto -> {
                    return com.twothree.backend.entity.Church.builder()
                        .id(churchDto.getId())
                        .name(churchDto.getName())
                        .address(churchDto.getAddress())
                        .phone(churchDto.getPhone())
                        .email(churchDto.getEmail())
                        .website(churchDto.getWebsite())
                        .pastorName(churchDto.getPastorName())
                        .pastorPhone(churchDto.getPastorPhone())
                        .pastorEmail(churchDto.getPastorEmail())
                        .status(churchDto.getStatus())
                        .build();
                })
                .collect(java.util.stream.Collectors.toList())
        );

        ByteArrayResource resource = new ByteArrayResource(excelStream.toByteArray());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=churches.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource);
    }

    @PostMapping("/get")
    public ResponseEntity<ChurchDto> getChurchById(@RequestBody ChurchIdRequest request) {
        return churchService.getChurchById(request.getChurchId())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/get-by-name")
    public ResponseEntity<ChurchDto> getChurchByName(@RequestBody ChurchNameRequest request) {
        return churchService.getChurchByName(request.getName())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/list-by-status")
    public ResponseEntity<List<ChurchDto>> getChurchesByStatus(@RequestBody ChurchStatusRequest request) {
        List<ChurchDto> churches = churchService.getChurchesByStatus(request.getStatus());
        return ResponseEntity.ok(churches);
    }

    @PostMapping("/search")
    public ResponseEntity<List<ChurchDto>> searchChurches(@RequestBody ChurchSearchRequest request) {
        List<ChurchDto> churches = churchService.searchChurches(request.getKeyword());
        return ResponseEntity.ok(churches);
    }

    @PostMapping("/create")
    public ResponseEntity<ChurchDto> createChurch(@RequestBody ChurchDto churchDto) {
        if (churchService.existsByName(churchDto.getName())) {
            return ResponseEntity.badRequest().build();
        }

        if (churchDto.getEmail() != null && churchService.existsByEmail(churchDto.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        ChurchDto createdChurch = churchService.createChurch(churchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChurch);
    }

    @PostMapping("/update")
    public ResponseEntity<ChurchDto> updateChurch(@RequestBody ChurchUpdateRequest request) {
        return churchService.updateChurch(request.getChurchId(), request.getChurchDto())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteChurch(@RequestBody ChurchIdRequest request) {
        boolean deleted = churchService.deleteChurch(request.getChurchId());
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/check-name")
    public ResponseEntity<Boolean> checkChurchNameExists(@RequestBody ChurchNameRequest request) {
        boolean exists = churchService.existsByName(request.getName());
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkChurchEmailExists(@RequestBody ChurchEmailRequest request) {
        boolean exists = churchService.existsByEmail(request.getEmail());
        return ResponseEntity.ok(exists);
    }
} 