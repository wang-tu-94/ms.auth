package com.myproject.ms.auth.controller;

import com.myproject.ms.auth.config.JwtConfig;
import com.myproject.ms.auth.config.SecurityConfig;
import com.myproject.ms.auth.dto.JwtResponse;
import com.myproject.ms.auth.dto.LoginRequest;
import com.myproject.ms.auth.repository.ServiceAccountRepository;
import com.myproject.ms.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtConfig.class})
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private ServiceAccountRepository serviceAccountRepository;

    @Test
    void login_ShouldReturnToken_WhenValidRequest() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmail("john@email.com");
        request.setPassword("password123");

        JwtResponse jwtResponse = new JwtResponse("mockedJwtToken");
        Mockito.when(authService.login(any(LoginRequest.class))).thenReturn(jwtResponse);

        // When + Then
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedJwtToken"));
    }

    @Test
    void login_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        // Given - Missing fields
        LoginRequest invalidRequest = new LoginRequest();

        // When + Then
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}