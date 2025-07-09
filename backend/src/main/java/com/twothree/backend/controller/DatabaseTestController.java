package com.twothree.backend.controller;

import com.twothree.backend.entity.Church;
import com.twothree.backend.entity.Department;
import com.twothree.backend.entity.Member;
import com.twothree.backend.entity.User;
import com.twothree.backend.repository.ChurchRepository;
import com.twothree.backend.repository.DepartmentRepository;
import com.twothree.backend.repository.MemberRepository;
import com.twothree.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/db-test")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DatabaseTestController {

    private final ChurchRepository churchRepository;
    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Database connection is working");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/churches")
    public ResponseEntity<List<Church>> getAllChurches() {
        List<Church> churches = churchRepository.findAll();
        return ResponseEntity.ok(churches);
    }

    @GetMapping("/churches/{id}")
    public ResponseEntity<Church> getChurchById(@PathVariable Long id) {
        return churchRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/departments/church/{churchId}")
    public ResponseEntity<List<Department>> getDepartmentsByChurch(@PathVariable Long churchId) {
        List<Department> departments = departmentRepository.findByChurchId(churchId);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/members/church/{churchId}")
    public ResponseEntity<List<Member>> getMembersByChurch(@PathVariable Long churchId) {
        List<Member> members = memberRepository.findByChurchId(churchId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDatabaseStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalChurches", churchRepository.count());
        stats.put("totalDepartments", departmentRepository.count());
        stats.put("totalMembers", memberRepository.count());
        stats.put("totalUsers", userRepository.count());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/church-with-departments/{churchId}")
    public ResponseEntity<Map<String, Object>> getChurchWithDepartments(@PathVariable Long churchId) {
        return churchRepository.findById(churchId)
                .map(church -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("church", church);
                    result.put("departments", departmentRepository.findByChurchId(churchId));
                    result.put("members", memberRepository.findByChurchId(churchId));
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 