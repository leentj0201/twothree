
package com.twothree.backend.controller;

import com.twothree.backend.dto.UserDto;
import com.twothree.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.twothree.backend.entity.User;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    // Request body for single ID
    private static class IdRequest {
        public Long id;
    }

    // Request body for create user
    private static class CreateUserRequest {
        public UserDto userDto;
        public String password;
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/get")
    public ResponseEntity<UserDto> getUserById(@RequestBody IdRequest request) {
        return userService.getUserById(request.id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request) {
        UserDto createdUser = userService.createUser(request.userDto, request.password);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody Map<String, Object> request) {
        Long id = ((Number) request.get("id")).longValue();
        Map<String, Object> dtoMap = (Map<String, Object>) request.get("userDto");
        UserDto userDto = new UserDto();
        userDto.setEmail((String) dtoMap.get("email"));
        userDto.setFullName((String) dtoMap.get("fullName"));
        userDto.setRole(User.Role.valueOf((String) dtoMap.get("role")));

        return userService.updateUser(id, userDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody IdRequest request) {
        boolean deleted = userService.deleteUser(request.id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
