package com.service.security;

public class SecurityConstants {
    public static final String JWT_SECRET = "asdhoi00as9dh1ho8717djamlu17jl1lo8aoakn8asdi110papso";
    public static final long EXPIRATION_TIME = 36_000_000; // 10 hours
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final String BASIC_TOKEN_PREFIX =  "Basic ";
    public static final String AUTH_HEADER = "Authorization";
    public static final String LOGIN_URI_ENDING = "/security/login";
    public static final String REGISTER_URI_ENDING = "/security/register";

    public static final String USER_ROLE = "ROLE_USER";
    public static final String MANAGER_ROLE = "ROLE_MANAGER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
}
