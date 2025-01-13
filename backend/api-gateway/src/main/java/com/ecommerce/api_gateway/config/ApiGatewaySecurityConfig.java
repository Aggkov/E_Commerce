package com.ecommerce.api_gateway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity(securedEnabled = true)
public class ApiGatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges ->
                        exchanges
//                                .pathMatchers("/**").permitAll() // Public access to all endpoints
                                // You can customize access control for specific endpoints, e.g.:
                                 .pathMatchers("/api/v1/core/products/admin/**").authenticated()

//                                 .pathMatchers("/swagger-ui/**").hasRole("ADMIN")
//                                 .pathMatchers("/swagger-ui.html").hasRole("ADMIN")
//                                 .pathMatchers("/api-docs").hasRole("ADMIN")
                                .anyExchange().permitAll() // All other requests permit them
                )
//                .oauth2Login(withDefaults())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())) // Use JWT with Keycloak
//                )

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.applyPermitDefaultValues();
        corsConfig.setAllowedOrigins(List.of("http://localhost:4200", "https://localhost:4200")); // Restricting origins as needed
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfig.setAllowedHeaders(List.of(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_DISPOSITION
//                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
        ));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Apply CORS config to all endpoints
        return source;
    }
}

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//               .csrf(HttpSecurity::disable)
//               .authorizeExchange(exchange ->
//                       exchange.pathMatchers("/**")
//                                .permitAll()
//                               .anyExchange()
//                               .authenticated()
//               )
//
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/**")
//                                .permitAll()
//                               .requestMatchers("/api/v1/core/**").permitAll()
//                               .requestMatchers("/api/v1/core/products/admin/**").hasRole("ADMIN")  // Only admin role can access
//                               .requestMatchers("/swagger-ui/**").hasRole("ADMIN")  // Only admin role can access
//                               .requestMatchers("/swagger-ui.html").hasRole("ADMIN")  // Only admin role can access
//                               .requestMatchers("/api-docs/**").hasRole("ADMIN")
//
//                              .anyRequest().authenticated() // All other requests are public
//                )
//                .oauth2ResourceServer(auth ->
//                auth.jwt(token ->
//                        token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter()))
//        );
//
//        return httpSecurity.build();
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


    /* JWT Token when admin logs in
    {
   "exp": 1727547287,
   "iat": 1727546987,
   "auth_time": 1727546987,
   "jti": "2723c1ce-9aab-46bf-86d9-f0554d66b9c2",
   "iss": "http://localhost:9090/realms/E-Commerce",
   "aud": "account",
   "sub": "0aa0ae2c-96e2-4f8c-a161-66b8c743d49a",
   "typ": "Bearer",
   "azp": "frontend",
   "sid": "125a9743-5e70-47a8-bd56-84082d259eeb",
   "acr": "1",
   "allowed-origins": [
      "*"
   ],
   "realm_access": {
      "roles": [
         "default-roles-e-commerce",
         "offline_access",
         "uma_authorization"
      ]
   },
   "resource_access": {
      "frontend": {
         "roles": [
            "admin",
            "user"
         ]
      },
      "account": {
         "roles": [
            "manage-account",
            "manage-account-links",
            "view-profile"
         ]
      }
   },
   "scope": "openid profile email",
   "email_verified": true,
   "name": "admin admin",
   "preferred_username": "t_admin",
   "given_name": "admin",
   "family_name": "admin",
   "email": "admin@email.com"
}
     */
