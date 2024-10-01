package com.me.ecommerce.config;

import com.me.ecommerce.config.KeycloakJwtAuthenticationConverter.KeycloakJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Enable CORS with default settings
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless authentication (JWT)

                // Configure authorization rules
                .authorizeHttpRequests(req -> req
                                // Permit all Swagger-related endpoints
                                .requestMatchers(
                                        "/api-docs/**",
//                                        "api/v2/api-docs/**",
//                                        "api/v3/api-docs/**",
//                                        "api/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "/**"  // Public API access
                                        //  "/**"
                                ).permitAll()
                                // Restrict "/admin/**" to users with "admin" role
//                                .requestMatchers("/admin/**").hasRole("admin")
                                // Require authentication for all other requests
                                .anyRequest().authenticated()
                )
                // Configure OAuth2 Resource Server with JWT and Keycloak converter
                .oauth2ResourceServer(auth ->
                        auth.jwt(token -> token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter()))
                );

        return http.build();

    }
}
