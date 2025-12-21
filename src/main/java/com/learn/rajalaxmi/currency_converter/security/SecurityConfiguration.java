package com.learn.rajalaxmi.currency_converter.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Checking access permission for incoming request...");
        http
                // Disable CSRF protection specifically for the h2-console path
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))

                // Allow the h2-console to use frames within the same origin
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                // Permit all requests to the h2-console path
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated() // Protect other endpoints
                );

        logger.info("Returning request with access rules...");
        return http.build();
    }
}
