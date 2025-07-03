package com.sushant_task_management.task_user_service.service;

import com.sushant_task_management.task_user_service.dto.UserInfo;
import com.sushant_task_management.task_user_service.entity.User;

import java.util.List;

public interface UserService {
    User getProfile(String token);
    List<UserInfo> getAllUser();
}
