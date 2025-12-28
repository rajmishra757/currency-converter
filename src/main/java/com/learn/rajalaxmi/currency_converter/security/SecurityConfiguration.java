package com.learn.rajalaxmi.currency_converter.security;

import com.learn.rajalaxmi.currency_converter.auth.ApiKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

@Slf4j
@Configuration
public class SecurityConfiguration {

    /**
     * Tells Spring Security explicitly the access permission rules
     * for different paths.
     *
     * @param http HttpSecurity instance representing the current request
     * @return Same HttpSecurity instance received as argument
     * with access permission rules defined
     * @throws Exception For any unexpected failure
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ApiKeyService apiKeyService) throws Exception {
        log.info("SecurityConfiguration.securityFilterChain >>> START");
        ApiKeyAuthFilter apiKeyAuthFilter = new ApiKeyAuthFilter(apiKeyService);

        http
                // Disable CSRF protection specifically for the h2-console path
                .csrf(AbstractHttpConfigurer::disable)

                // Allows the app to be stateless
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Explicitly set the security context repository to NOT use sessions
                .securityContext(context -> context
                        .securityContextRepository(new RequestAttributeSecurityContextRepository())
                )

                // Allow the h2-console to use frames within the same origin
                //.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                // Permit all requests to the h2-console path
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/actuator/health", "/h2-console/**", "/api/currency/currencies").permitAll()
                        .requestMatchers("/api/currency/convert", "/api/currency/history").hasRole("API")
                        .anyRequest().authenticated() // Protect other endpoints
                )

                // Applies filter before passing through this configuration
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("SecurityConfiguration.securityFilterChain <<< END");
        return http.build();
    }
}
