package com.learn.rajalaxmi.currency_converter.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @InjectMocks
    private ApiKeyService apiKeyService;

    @BeforeEach
    void setUp() {}

    @Test
    void validate_shouldReturnEmpty_whenKeyIsNull() {
        Optional<ApiKey> result = apiKeyService.validate(null);
        assertTrue(result.isEmpty());
        verifyNoInteractions(apiKeyRepository);
    }

    @Test
    void validate_shouldReturnEmpty_whenKeyIsEmpty() {
        Optional<ApiKey> result = apiKeyService.validate("");
        assertTrue(result.isEmpty());
        verifyNoInteractions(apiKeyRepository);
    }

    @Test
    void validate_shouldReturnEmpty_whenKeyIsBlank() {
        Optional<ApiKey> result = apiKeyService.validate("  ");
        assertTrue(result.isEmpty());
        verifyNoInteractions(apiKeyRepository);
    }

    @Test
    void verify_shouldReturnEmpty_whenKeyNotFound() {
        when(apiKeyRepository.findByKeyValue("invalid-key"))
                .thenReturn(Optional.empty());

        Optional<ApiKey> result = apiKeyService.validate("invalid-key");
        assertTrue(result.isEmpty());
        verify(apiKeyRepository).findByKeyValue("invalid-key");
    }

    @Test
    void verify_shouldReturnEmpty_whenKeyIsInactive() {
        ApiKey inactiveApiKey = mock(ApiKey.class);
        when(inactiveApiKey.isActive()).thenReturn(false);
        when(apiKeyRepository.findByKeyValue("inactive-key"))
                .thenReturn(Optional.of(inactiveApiKey));

        Optional<ApiKey> result = apiKeyService.validate("inactive-key");
        assertTrue(result.isEmpty());
        verify(apiKeyRepository).findByKeyValue("inactive-key");
    }

    @Test
    void verify_shouldReturnApiKey_whenKeyIsActive() {
        ApiKey activeApiKey = mock(ApiKey.class);
        when(activeApiKey.isActive()).thenReturn(true);
        when(apiKeyRepository.findByKeyValue("active-key"))
                .thenReturn(Optional.of(activeApiKey));

        Optional<ApiKey> result = apiKeyService.validate("active-key");
        assertTrue(result.isPresent());
        assertEquals(activeApiKey, result.get());
        verify(apiKeyRepository).findByKeyValue("active-key");
    }
}
