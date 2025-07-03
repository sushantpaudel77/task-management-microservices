package com.sushant_task_management.task_user_service.service;

import com.sushant_task_management.task_user_service.config.JwtTokenProvider;
import com.sushant_task_management.task_user_service.dto.UserInfo;
import com.sushant_task_management.task_user_service.entity.User;
import com.sushant_task_management.task_user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public User getProfile(String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    public List<UserInfo> getAllUser() {
        return userRepository.findAll()
                .stream().map(this::matToDto)
                .toList();
    }

    private UserInfo matToDto(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }
}
