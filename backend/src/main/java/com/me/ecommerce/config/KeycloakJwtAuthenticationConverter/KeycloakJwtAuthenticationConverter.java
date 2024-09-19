package com.me.ecommerce.config.KeycloakJwtAuthenticationConverter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
        var account = new HashMap<>(jwt.getClaimAsMap("resource_access.account"));

        // (Map<?, ?>)
//        var eternal = resourceAccess.getClaimAsMap("account");

        var roles = (List<?>) account.get("roles");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(
                        "ROLE_" + ((String) role).replace("-", "_"))
                )
                .collect(toSet());
    }
}
