package com.myproject.ms.auth.service;

import com.myproject.ms.auth.dto.AccountDto;
import com.myproject.ms.auth.dto.RegisterRequest;
import com.myproject.ms.auth.exception.BadRequestException;
import com.myproject.ms.auth.mapper.AccountMapper;
import com.myproject.ms.auth.model.Account;
import com.myproject.ms.auth.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountDto register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Un compte existe déjà avec cet email.");
        }

        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Un compte existe déjà avec ce nom d'utilisateur.");
        }

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        return accountMapper.toDto(accountRepository.save(account));
    }
}
