package com.learn.rajalaxmi.currency_converter.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    @Override
    public Optional<ApiKeyDto> validate(String keyValue) {
        log.info("API key received for validation: {}", keyValue);

        if(keyValue == null || keyValue.isBlank()) {
            log.info("Received API key is null/blank");
            return Optional.empty();
        }

        log.info("Validating API key...");
        return apiKeyRepository.findByKeyValue(keyValue)
                .filter(ApiKey::isActive)
                .map(ApiKeyDto::fromEntity);
    }
}
