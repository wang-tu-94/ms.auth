package com.myproject.ms.auth.service;


import com.myproject.ms.auth.dto.AccountDto;
import com.myproject.ms.auth.dto.RegisterRequest;

public interface AccountService {
    AccountDto register(RegisterRequest request);
}
