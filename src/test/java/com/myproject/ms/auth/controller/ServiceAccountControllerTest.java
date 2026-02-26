package com.myproject.ms.auth.controller;

import com.myproject.ms.auth.config.JwtConfig;
import com.myproject.ms.auth.config.SecurityConfig;
import com.myproject.ms.auth.dto.JwtResponse;
import com.myproject.ms.auth.dto.ServiceAccountDto;
import com.myproject.ms.auth.repository.ServiceAccountRepository;
import com.myproject.ms.auth.service.ServiceAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceAccountController.class)
@Import({SecurityConfig.class, JwtConfig.class})
class ServiceAccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceAccountService serviceAccountService;

    @MockitoBean
    private ServiceAccountRepository serviceAccountRepository;

    @Test
    void createServiceAccount_ShouldReturnToken_WhenValidRequest() throws Exception {
        ServiceAccountDto request = new ServiceAccountDto(null, "App-Interne", null, null);

        when(serviceAccountService.createServiceAccount(any(ServiceAccountDto.class))).thenReturn(new JwtResponse("mockedJwtToken"));

        mockMvc.perform(post("/v1/service-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("mockedJwtToken"));

    }

    @Test
    void createServiceAccount_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        // Given - Missing fields
        ServiceAccountDto invalidRequest = new ServiceAccountDto(null, "", null, null);

        // When + Then
        mockMvc.perform(post("/v1/service-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

}