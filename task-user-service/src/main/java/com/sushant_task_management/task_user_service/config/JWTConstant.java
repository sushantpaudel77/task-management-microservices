package com.sushant_task_management.task_user_service.config;

public class JWTConstant {

    private JWTConstant() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY") != null
            ? System.getenv("JWT_SECRET_KEY")
            : "1003e07723878de724f4c37ca500194f2ff1eea2f7c3e634";

    public static final String JWT_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long TOKEN_EXPIRATION_TIME = 86400000; // 24 hours
    public static final String AUTHORITIES_KEY = "authorities";
    public static final String EMAIL_KEY = "email";
}
