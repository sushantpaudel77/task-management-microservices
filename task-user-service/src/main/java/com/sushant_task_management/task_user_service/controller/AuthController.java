package com.sushant_task_management.task_user_service.controller;

import com.sushant_task_management.task_user_service.config.JwtTokenProvider;
import com.sushant_task_management.task_user_service.dto.AuthResponse;
import com.sushant_task_management.task_user_service.dto.LoginRequest;
import com.sushant_task_management.task_user_service.entity.User;
import com.sushant_task_management.task_user_service.repository.UserRepository;
import com.sushant_task_management.task_user_service.service.CustomerUserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerUserServiceImpl customerUserService;
    private final JwtTokenProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody User user
    ) {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String role = user.getRole();
        user.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        userRepository.save(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authentication(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Successfully");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    private Authentication authentication(String username, String password) {
        UserDetails userDetails = customerUserService.loadUserByUsername(username);
        log.info("sign in userDetails {}", userDetails);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username and password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username and password");
        }
        return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
    }
}
