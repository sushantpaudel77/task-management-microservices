package com.sushant_task_management.task_user_service.controller;

import com.sushant_task_management.task_user_service.repository.UserRepository;
import com.sushant_task_management.task_user_service.service.CustomerUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private CustomerUserServiceImpl customerUserService;
}
