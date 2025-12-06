package com.keep.notes.security;

import com.keep.common.security.JwtAuthenticationFilter;
import com.keep.common.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationFilter(jwtTokenProvider, (token, claims, request) -> {
            Object uidObj = claims.get("uid");
            Long userId = null;
            if (uidObj instanceof Number) {
                userId = ((Number) uidObj).longValue();
            } else if (uidObj instanceof String) {
                userId = Long.valueOf((String) uidObj);
            }

            String email = claims.getSubject();

            if (userId == null || email == null || email.isBlank()) {
                return null;
            }

            JwtUserPrincipal principal = new JwtUserPrincipal(userId, email);

            var authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    principal.getAuthorities()
            );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            return authentication;
        });
    }
}
