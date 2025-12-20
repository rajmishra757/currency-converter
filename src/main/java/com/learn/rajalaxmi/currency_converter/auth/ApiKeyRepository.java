package com.learn.rajalaxmi.currency_converter.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    /**
     * Returns an ApiKey instance for the given key
     * or null if key does not exist.
     *
     * @param keyValue String representing the unique key
     * @return A nullable optional ApiKey corresponding to the given key
     */
    Optional<ApiKey> findByKeyValue(String keyValue);
}
