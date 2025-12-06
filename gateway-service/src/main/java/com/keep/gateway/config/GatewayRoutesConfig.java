package com.keep.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Auth Service
            .route("auth-service", r -> r
                .path("/api/v1/auth/**")
                .uri("http://localhost:8081"))

            // Notes Service
            .route("notes-service", r -> r
                .path("/api/v1/notes/**")
                .uri("http://localhost:8082"))

            // Reminder Service
            .route("reminder-service", r -> r
                .path("/api/v1/reminders/**")
                .uri("http://localhost:8083"))

            // Attachment Service
            .route("attachment-service", r -> r
                .path("/api/v1/attachments/**")
                .uri("http://localhost:8084"))

            // Search Service
            .route("search-service", r -> r
                .path("/api/v1/search/**")
                .uri("http://localhost:8086"))

            .build();
    }
}
