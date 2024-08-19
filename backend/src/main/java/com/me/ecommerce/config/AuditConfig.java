package com.me.ecommerce.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

//    @Bean
//    public AuditorAware<Long> auditorProvider() {
//        return new SpringSecurityAuditAwareImpl(); // Implement this to return the current user ID
//    }
//
//    class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {
//
//        @Override
//        public Optional<Long> getCurrentAuditor() {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
//                return Optional.empty();
//            }
//
//            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//
//            return Optional.ofNullable(userPrincipal.getId());
//        }
//    }
}
