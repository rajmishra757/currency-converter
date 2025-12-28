package com.learn.rajalaxmi.currency_converter.auth;

import java.util.Optional;

public interface ApiKeyService {

    Optional<ApiKeyDto> validate(String keyValue);
}
