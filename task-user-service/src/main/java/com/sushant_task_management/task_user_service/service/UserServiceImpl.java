package com.sushant_task_management.task_user_service.service;

import com.sushant_task_management.task_user_service.config.JwtTokenProvider;
import com.sushant_task_management.task_user_service.entity.User;
import com.sushant_task_management.task_user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
