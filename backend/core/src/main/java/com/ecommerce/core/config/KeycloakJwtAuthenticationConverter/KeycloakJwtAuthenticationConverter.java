package com.ecommerce.core.config.KeycloakJwtAuthenticationConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import static java.util.stream.Collectors.toSet;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final Logger logger = Logger.getLogger(KeycloakJwtAuthenticationConverter.class.getName());

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        return new JwtAuthenticationToken(
                source,
                Stream.concat(
                                new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                                extractResourceRoles(source).stream())
                        .collect(toSet()));
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

//        var account = new HashMap<>(jwt.getClaimAsMap("resource_access.frontend"));
//        System.out.println("JWT Account Claims: " + account); // Log claims
//
//        var roles = (List<?>) account.get("roles");
//
//        return roles.stream()
//                .map(role -> {
//                    String authority = "ROLE_" + ((String) role).replace("-", "_");
//                    logger.info("Mapping role to authority: " + authority);
//                    return new SimpleGrantedAuthority(authority);
//                })
//                .collect(toSet());
    }

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
}
