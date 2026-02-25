package com.myproject.ms.auth.service;


import com.myproject.ms.auth.dto.JwtResponse;
import com.myproject.ms.auth.dto.ServiceAccountDto;

public interface ServiceAccountService {
    JwtResponse createServiceAccount(ServiceAccountDto request);
}
