package com.ecommerce.core.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import static java.util.stream.Collectors.toSet;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless authentication (JWT)

                // Configure authorization rules
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/core/products/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/swagger-ui/**").hasRole("ADMIN")
//                                .requestMatchers("/swagger-ui.html").hasRole("ADMIN")
//                                .requestMatchers("/api-docs/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(token -> token.jwtAuthenticationConverter(
                        new KeycloakJwtAuthenticationConverter())));

        return http.build();
    }

    static class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        private final Logger logger = Logger.getLogger(KeycloakJwtAuthenticationConverter.class.getName());

        @Override
        public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
            Collection<GrantedAuthority> authorities = Stream.concat(
                    new JwtGrantedAuthoritiesConverter().convert(jwt).stream(),
                    extractResourceRoles(jwt).stream()
            ).collect(toSet());

            return new JwtAuthenticationToken(jwt, authorities);
        }

        private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
            // Extract the resource_access map from the JWT
            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");

            // Check if resource_access or frontend is missing
            if (resourceAccess == null || !resourceAccess.containsKey("frontend")) {
                logger.warning("No resource access or frontend client roles found in the token");
                return Collections.emptyList(); // No roles to return
            }

            // Extract the frontend client access from resource_access
            Map<String, Object> frontendAccess = (Map<String, Object>) resourceAccess.get("frontend");

            // Extract roles for the frontend client
            List<?> roles = (List<?>) frontendAccess.get("roles");

            if (roles == null) {
                logger.warning("No roles found for frontend in the token");
                return Collections.emptyList(); // No roles found
            }

            // Map the roles to GrantedAuthority objects
            Set<GrantedAuthority> grantedAuthorities = roles.stream()
                    .map(role -> {
                        String authority = "ROLE_" + ((String) role).replace("-", "_").toUpperCase(); // Ensure consistent formatting
                        logger.info("Mapping role to authority: " + authority);
                        return new SimpleGrantedAuthority(authority);
                    })
                    .collect(Collectors.toSet());
            return grantedAuthorities;
        }
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
////        corsConfig.setAllowedOrigins(List.of("https://localhost:4200")); // Restricting origins as needed
//        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
//        corsConfig.setAllowedHeaders(List.of(
//                HttpHeaders.ORIGIN,
//                HttpHeaders.CONTENT_TYPE,
//                HttpHeaders.ACCEPT,
//                HttpHeaders.AUTHORIZATION,
//                HttpHeaders.CONTENT_DISPOSITION
////                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
//        ));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig); // Apply CORS config to all endpoints
//        return source;
//    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
//        config.setAllowedHeaders(Arrays.asList(
//                HttpHeaders.ORIGIN,
//                HttpHeaders.CONTENT_TYPE,
//                HttpHeaders.ACCEPT,
//                HttpHeaders.AUTHORIZATION,
//                HttpHeaders.CONTENT_DISPOSITION,
//                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
//        ));
//        config.setAllowedMethods(Arrays.asList(
//                "GET",
//                "POST",
//                "DELETE",
//                "PUT",
//                "PATCH"
//        ));
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
}
