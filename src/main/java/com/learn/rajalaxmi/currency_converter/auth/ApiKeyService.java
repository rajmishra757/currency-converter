package com.learn.rajalaxmi.currency_converter.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiKeyService {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyService.class);

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public Optional<ApiKey> validate(String keyValue) {
        logger.info("API key received for validation: {}", keyValue);

        if(keyValue == null || keyValue.isBlank()) {
            logger.info("Received API key is null/blank");
            return Optional.empty();
        }

        logger.info("Validating API key...");
        return apiKeyRepository.findByKeyValue(keyValue)
                .filter(ApiKey::isActive);
    }
}
