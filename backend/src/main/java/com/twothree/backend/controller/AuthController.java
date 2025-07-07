package com.twothree.backend.controller;

import com.twothree.backend.dto.UserDto;
import com.twothree.backend.dto.UserInfoRequest;
import com.twothree.backend.service.AuthService;
import com.twothree.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final UserService userService;
    
    @PostMapping("/signup")
    public ResponseEntity<UserDto.SignInResponse> signUp(@RequestBody UserDto.SignUpRequest request) {
        UserDto.SignInResponse response = authService.signUp(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/signin")
    public ResponseEntity<UserDto.SignInResponse> signIn(@RequestBody UserDto.SignInRequest request) {
        UserDto.SignInResponse response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/me")
    public ResponseEntity<UserDto.UserInfo> getCurrentUser(@RequestBody UserInfoRequest request) {
        UserDto.UserInfo userInfo = userService.getUserInfo(request.getUsername());
        return ResponseEntity.ok(userInfo);
    }
} 