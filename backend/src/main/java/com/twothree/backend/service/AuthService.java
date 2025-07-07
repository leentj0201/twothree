package com.twothree.backend.service;

import com.twothree.backend.dto.UserDto;
import com.twothree.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    
    public UserDto.SignInResponse signIn(UserDto.SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        
        return UserDto.SignInResponse.builder()
                .token(token)
                .user(UserDto.UserInfo.from(user))
                .build();
    }
    
    public UserDto.SignInResponse signUp(UserDto.SignUpRequest request) {
        UserDto.UserInfo userInfo = userService.signUp(request);
        
        User user = User.builder()
                .id(userInfo.getId())
                .username(userInfo.getUsername())
                .email(userInfo.getEmail())
                .fullName(userInfo.getFullName())
                .role(userInfo.getRole())
                .build();
        
        String token = jwtService.generateToken(user);
        
        return UserDto.SignInResponse.builder()
                .token(token)
                .user(userInfo)
                .build();
    }
} 