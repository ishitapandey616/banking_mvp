package com.nagarro.apigateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("customer-service", route -> route
                .path("/customer/**")
                .uri("http://customer-service:8001"))
            .route("account-service", route -> route
                .path("/account/**")
                .uri("http://account-service:8002"))
            .build();
    }
}
