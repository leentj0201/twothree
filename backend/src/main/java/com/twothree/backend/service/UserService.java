
package com.twothree.backend.service;

import com.twothree.backend.dto.UserDto;
import com.twothree.backend.entity.User;
import com.twothree.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(UserDto userDto, String password) {
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(password))
                .email(userDto.getEmail())
                .fullName(userDto.getFullName())
                .role(userDto.getRole() != null ? userDto.getRole() : User.Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        return UserDto.fromEntity(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(UserDto::fromEntity);
    }

    @Transactional
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setEmail(userDto.getEmail());
                    existingUser.setFullName(userDto.getFullName());
                    existingUser.setRole(userDto.getRole());
                    // Note: Username and password updates are handled separately for security reasons
                    return UserDto.fromEntity(userRepository.save(existingUser));
                });
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public UserDto.UserInfo getUserInfo(String username) {
        return userRepository.findByUsername(username)
                .map(UserDto.UserInfo::from)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public UserDto.UserInfo signUp(UserDto.SignUpRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(request.getRole() != null ? request.getRole() : User.Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        return UserDto.UserInfo.from(savedUser);
    }
}
