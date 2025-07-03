package com.sushant_task_management.task_user_service.service;

import com.sushant_task_management.task_user_service.config.JwtTokenProvider;
import com.sushant_task_management.task_user_service.dto.AuthResponse;
import com.sushant_task_management.task_user_service.dto.LoginRequest;
import com.sushant_task_management.task_user_service.dto.RegisterRequest;
import com.sushant_task_management.task_user_service.dto.UserInfo;
import com.sushant_task_management.task_user_service.entity.Role;
import com.sushant_task_management.task_user_service.entity.User;
import com.sushant_task_management.task_user_service.exception.UserAlreadyExistsException;
import com.sushant_task_management.task_user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobileNumber(request.getMobileNumber())
                .role(Role.valueOf(request.getRole()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + savedUser.getRole().name()))
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .jwt(token)
                .message("Registration successful")
                .status(true)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .userInfo(UserInfo.builder()
                        .id(savedUser.getId())
                        .fullName(savedUser.getFullName())
                        .email(savedUser.getEmail())
                        .role(savedUser.getRole().name())
                        .build())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("User logged in successfully: {}", user.getEmail());

        return AuthResponse.builder()
                .jwt(token)
                .message("Login successful")
                .status(true)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .userInfo(UserInfo.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .build();
    }

    public AuthResponse refreshToken(String token) {
        String cleanToken = token.replace("Bearer ", "");

        if (!jwtTokenProvider.validateToken(cleanToken)) {
            throw new RuntimeException("Invalid or expired token");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(cleanToken);
        String newToken = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .jwt(newToken)
                .message("Token refreshed successfully")
                .status(true)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .build();
    }
}
