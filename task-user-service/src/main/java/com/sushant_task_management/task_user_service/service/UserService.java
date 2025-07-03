package com.sushant_task_management.task_user_service.service;

import com.sushant_task_management.task_user_service.entity.User;

public interface UserService {
    User getProfile(String token);
}
