package com.myproject.ms.auth.controller;

import com.myproject.ms.auth.config.JwtConfig;
import com.myproject.ms.auth.config.SecurityConfig;
import com.myproject.ms.auth.dto.AccountDto;
import com.myproject.ms.auth.dto.RegisterRequest;
import com.myproject.ms.auth.repository.AccountRepository;
import com.myproject.ms.auth.repository.ServiceAccountRepository;
import com.myproject.ms.auth.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@Import({SecurityConfig.class, JwtConfig.class})
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private ServiceAccountRepository serviceAccountRepository;

    private RegisterRequest request;

    private AccountDto accountDto;

    @BeforeEach
    void setUp() {
        request = new RegisterRequest();
        request.setUsername("john");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setUsername("john");
        accountDto.setEmail("john@example.com");
    }
    @Test
    void register_shouldReturnCreatedAccount_whenValidRequest() throws Exception {
        Mockito.when(accountService.register(any(RegisterRequest.class))).thenReturn(accountDto);

        mockMvc.perform(post("/v1/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void register_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest();

        mockMvc.perform(post("/v1/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}