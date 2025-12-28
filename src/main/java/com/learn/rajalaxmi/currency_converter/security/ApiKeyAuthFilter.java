package com.learn.rajalaxmi.currency_converter.security;

import com.learn.rajalaxmi.currency_converter.auth.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    /**
     * Reads X-API-Key, calls the service, and sets Authentication if valid.
     * Otherwise, it returns a structured 401.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @param chain    Filter chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("ApiKeyAuthFilter.doFilter >>> START");

        String apiKey = request.getHeader("X-API-Key");

        // 1. If header is missing, pass to next filter.
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("No auth set. Security config will decide the next move.");
            chain.doFilter(request, response);
            return;
        }

        var recordOptional = apiKeyService.validate(apiKey);

        // 2. If key is present but INVALID, return 401
        if (recordOptional.isEmpty()) {
            log.warn("No auth key found. Unauthorized access.");
            writeUnauthorized(response, "Invalid or revoked API key");
            return;
        }

        // 3. If key is VALID, set authentication
        log.info("Setting up authentication");
        var recordKey = recordOptional.get();
        Authentication auth = new ApiKeyAuthentication(recordKey.owner(), recordKey.id());
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
        log.info("ApiKeyAuthFilter.doFilter <<< END");
    }

    /**
     * Ensures the logic does not even touch the public endpoints.
     *
     * @param request HTTP request
     * @return TRUE if the requested enpoint is public
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Skip filter execution for public endpoints to prevent side effects
        return path.equals("/actuator/health") ||
                path.equals("/h2-console") ||
                path.equals("/api/currency/currencies");
    }

    /**
     * Writes an unauthorized response.
     *
     * @param response HTTP response
     * @param message  Message coveying an unauthorized access
     * @throws IOException
     */
    private void writeUnauthorized(@NotNull HttpServletResponse response, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String body = """
                {
                    "code": "UNAUTHORIZED",
                    "message": "%s",
                    "timestamp": "%s",
                    "requestId": "%s"
                }
                """.formatted(message, Instant.now().toString(), UUID.randomUUID());
        response.getWriter().write(body);
    }

    static class ApiKeyAuthentication extends AbstractAuthenticationToken {

        private final String principalName;
        @Getter
        private final Long apiKeyId;

        ApiKeyAuthentication(String principalName, Long apiKeyId) {
            super(List.of(new SimpleGrantedAuthority("ROLE_API")));
            this.principalName = principalName;
            this.apiKeyId = apiKeyId;
            setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return "";
        }

        @Override
        public Object getPrincipal() {
            return this.principalName;
        }
    }
}
