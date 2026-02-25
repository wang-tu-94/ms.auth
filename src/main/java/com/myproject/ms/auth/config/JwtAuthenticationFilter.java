package com.myproject.ms.auth.config;

import com.myproject.ms.auth.model.ServiceAccount;
import com.myproject.ms.auth.repository.ServiceAccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final ServiceAccountRepository serviceAccountRepository;
    private final SecretKey key;

    public JwtAuthenticationFilter(JwtConfig jwtConfig, ServiceAccountRepository serviceAccountRepository) {
        this.serviceAccountRepository = serviceAccountRepository;
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getSecret()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace("Bearer ", "").trim();

        try {
            // Cette ligne fait TOUT le travail : validation de signature, expiration, etc.
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String subject = claims.getSubject();
            String tokenType = claims.get("type", String.class);

            if (subject != null) {
                if ("SERVICE".equals(tokenType)) {
                    Optional<ServiceAccount> serviceAccount = serviceAccountRepository.findByClientName(subject);

                    if (serviceAccount.isPresent() && serviceAccount.get().getActive()) {
                        setAuthenticationContext(subject, "ROLE_SERVICE_ACCOUNT");
                    } else {
                        log.warn("Compte de service inactif : {}", subject);
                        SecurityContextHolder.clearContext();
                        return;
                    }
                } else {
                    setAuthenticationContext(subject, "ROLE_USER");
                }
            }

        } catch (JwtException e) {
            log.error("Token JWT invalide : {}", e);
            SecurityContextHolder.clearContext();
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(String subject, String role) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                subject,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
