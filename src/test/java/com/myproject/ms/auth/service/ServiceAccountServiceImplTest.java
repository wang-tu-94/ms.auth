package com.myproject.ms.auth.service;

import com.myproject.ms.auth.config.JwtConfig;
import com.myproject.ms.auth.dto.ServiceAccountDto;
import com.myproject.ms.auth.exception.BadRequestException;
import com.myproject.ms.auth.model.ServiceAccount;
import com.myproject.ms.auth.repository.ServiceAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ServiceAccountServiceImplTest {
    @Mock
    private ServiceAccountRepository serviceAccountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private ServiceAccountServiceImpl serviceAccountService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(jwtConfig.getSecret()).thenReturn("8w9eK7sVh3FQ2mJ6lP0rXyZtN1bQ4uDkFvHsT2cW+Eo=");
    }

    @Test
    void createServiceAccount_Success() {
        // Arrange - Utilisation du record ServiceAccountDto
        ServiceAccountDto request = new ServiceAccountDto(null, "App-Interne", null, null);

        when(serviceAccountRepository.existsByClientName("App-Interne")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_token_xyz");

        // Act
        String token = serviceAccountService.createServiceAccount(request).getToken();

        // Assert
        assertNotNull(token);

        // On vérifie que le repository a bien reçu l'objet avec les bonnes valeurs
        ArgumentCaptor<ServiceAccount> captor = ArgumentCaptor.forClass(ServiceAccount.class);
        verify(serviceAccountRepository).save(captor.capture());

        ServiceAccount saved = captor.getValue();
        assertEquals("App-Interne", saved.getClientName());
        assertEquals("hashed_token_xyz", saved.getApiKeyHashed());
        assertTrue(saved.getActive());
    }

    @Test
    void createServiceAccount_AlreadyExists_ThrowsException() {
        // Arrange
        ServiceAccountDto request = new ServiceAccountDto(null, "App-Existing", null, null);
        when(serviceAccountRepository.existsByClientName("App-Existing")).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            serviceAccountService.createServiceAccount(request);
        });

        verify(serviceAccountRepository, never()).save(any());
    }
}