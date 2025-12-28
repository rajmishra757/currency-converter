package com.learn.rajalaxmi.currency_converter.auth;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public record ApiKeyDto(Long id, String keyValue, String owner, Boolean active, Instant createdAt, Instant revokedAt) {

    /**
     * Returns an ApiKeyDto instance for this entity.
     *
     * @param apiKey Entity instance
     * @return ApiKeyDto instance for this entity
     */
    @Contract("_ -> new")
    public static @NotNull ApiKeyDto fromEntity(@NotNull ApiKey apiKey) {
        return new ApiKeyDto(apiKey.getId(), apiKey.getKeyValue(), apiKey.getOwner(),
                apiKey.isActive(), apiKey.getCreatedAt(), apiKey.getRevokedAt());
    }

    /**
     * Returns an entity instance for this DTO.
     *
     * @return ApiKey entity instance
     */
    @Contract(" -> new")
    public @NotNull ApiKey toEntity() {
        return new ApiKey(this.id, this.keyValue, this.owner,
                this.active, this.createdAt, this.revokedAt);
    }
}
