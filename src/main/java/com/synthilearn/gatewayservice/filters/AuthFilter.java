package com.synthilearn.gatewayservice.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AuthFilter.Config config) {
        return ((exchange, chain) -> {

            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
