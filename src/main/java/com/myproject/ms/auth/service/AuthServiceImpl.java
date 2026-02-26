package com.myproject.ms.auth.service;

import com.myproject.ms.auth.config.JwtConfig;
import com.myproject.ms.auth.dto.JwtResponse;
import com.myproject.ms.auth.dto.LoginRequest;
import com.myproject.ms.auth.exception.ForbiddenException;
import com.myproject.ms.auth.mapper.AccountMapper;
import com.myproject.ms.auth.model.Account;
import com.myproject.ms.auth.repository.AccountRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public JwtResponse login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ForbiddenException("Email ou mot de passe invalide"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new ForbiddenException("Email ou mot de passe invalide");
        }

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getSecret()));

        String token = Jwts.builder()
                .setSubject(account.getId().toString())
                .claim("email", account.getEmail())
                .claim("username", account.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtConfig.getExpirationMs()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new JwtResponse(token, accountMapper.toDto(account));
    }
}
