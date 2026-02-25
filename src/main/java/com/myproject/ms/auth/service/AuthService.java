package com.myproject.ms.auth.service;


import com.myproject.ms.auth.dto.JwtResponse;
import com.myproject.ms.auth.dto.LoginRequest;

public interface AuthService {
    JwtResponse login(LoginRequest request);
}
