package com.learn.rajalaxmi.currency_converter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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

        return http.build();
    }
}
