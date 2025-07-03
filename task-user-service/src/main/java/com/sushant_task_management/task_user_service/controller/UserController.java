package com.sushant_task_management.task_user_service.controller;

import com.sushant_task_management.task_user_service.config.JWTConstant;
import com.sushant_task_management.task_user_service.dto.UserInfo;
import com.sushant_task_management.task_user_service.entity.User;
import com.sushant_task_management.task_user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserInfo> getUserProfile(@RequestHeader("Authorization") String bearerToken
    ) {
        String token = extractToken(bearerToken);
        var user = userService.getProfile(token);

        UserInfo userInfo = UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        var userInfos = userService.getAllUser();
        return ResponseEntity.ok(userInfos);
    }

    private String extractToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JWTConstant.TOKEN_PREFIX)) {
            return bearerToken.substring(JWTConstant.TOKEN_PREFIX.length()).trim();
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }

}
