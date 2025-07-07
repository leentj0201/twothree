package com.twothree.backend.controller;

import com.twothree.backend.dto.DepartmentDto;
import com.twothree.backend.entity.Department;
import com.twothree.backend.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.twothree.backend.dto.ChurchIdRequest;
import com.twothree.backend.dto.DepartmentListRequest;
import com.twothree.backend.dto.ParentDepartmentIdRequest;
import com.twothree.backend.dto.DepartmentIdRequest;
import com.twothree.backend.dto.ChurchIdKeywordRequest;
import com.twothree.backend.dto.DepartmentUpdateRequest;
import com.twothree.backend.dto.DepartmentNameChurchIdRequest;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    @PostMapping("/list")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments(@RequestBody(required = false) DepartmentListRequest request) {
        List<DepartmentDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    
    @PostMapping("/church/departments")
    public ResponseEntity<List<DepartmentDto>> getDepartmentsByChurchId(@RequestBody ChurchIdRequest req) {
        List<DepartmentDto> departments = departmentService.getDepartmentsByChurchId(req.getChurchId());
        return ResponseEntity.ok(departments);
    }
    
    @PostMapping("/active-by-church")
    public ResponseEntity<List<DepartmentDto>> getActiveDepartmentsByChurchId(@RequestBody ChurchIdRequest request) {
        List<DepartmentDto> departments = departmentService.getActiveDepartmentsByChurchId(request.getChurchId());
        return ResponseEntity.ok(departments);
    }
    
    @PostMapping("/root-by-church")
    public ResponseEntity<List<DepartmentDto>> getRootDepartmentsByChurchId(@RequestBody ChurchIdRequest request) {
        List<DepartmentDto> departments = departmentService.getRootDepartmentsByChurchId(request.getChurchId());
        return ResponseEntity.ok(departments);
    }
    
    @PostMapping("/sub")
    public ResponseEntity<List<DepartmentDto>> getSubDepartments(@RequestBody ParentDepartmentIdRequest request) {
        List<DepartmentDto> departments = departmentService.getSubDepartments(request.getParentDepartmentId());
        return ResponseEntity.ok(departments);
    }
    
    @PostMapping("/get")
    public ResponseEntity<DepartmentDto> getDepartmentById(@RequestBody DepartmentIdRequest request) {
        return departmentService.getDepartmentById(request.getDepartmentId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/search-by-church")
    public ResponseEntity<List<DepartmentDto>> searchDepartmentsByChurchId(@RequestBody ChurchIdKeywordRequest request) {
        List<DepartmentDto> departments = departmentService.searchDepartmentsByChurchId(request.getKeyword(), request.getChurchId());
        return ResponseEntity.ok(departments);
    }
    
    @PostMapping("/create")
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
        if (departmentService.existsByNameAndChurchId(departmentDto.getName(), departmentDto.getChurchId())) {
            return ResponseEntity.badRequest().build();
        }
        
        DepartmentDto createdDepartment = departmentService.createDepartment(departmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }
    
    @PostMapping("/update")
    public ResponseEntity<DepartmentDto> updateDepartment(@RequestBody DepartmentUpdateRequest request) {
        return departmentService.updateDepartment(request.getDepartmentId(), request.getDepartmentDto())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteDepartment(@RequestBody DepartmentIdRequest request) {
        boolean deleted = departmentService.deleteDepartment(request.getDepartmentId());
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/check-name")
    public ResponseEntity<Boolean> checkDepartmentNameExists(@RequestBody DepartmentNameChurchIdRequest request) {
        boolean exists = departmentService.existsByNameAndChurchId(request.getName(), request.getChurchId());
        return ResponseEntity.ok(exists);
    }
} 