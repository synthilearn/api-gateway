package com.synthilearn.gatewayservice.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private WebClient webClient;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AuthFilter.Config config) {
        return ((exchange, chain) -> {
            log.info("Sosich pisich");
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing Auth Header");
            }
            String token = exchange.getRequest().getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            try {
                Mono<String> responseMono = webClient.get()
                        .uri("http://localhost:8090/auth/validate?token=" + token)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .retrieve()
                        .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                                clientResponse -> Mono.error(new WebClientResponseException(
                                        clientResponse.statusCode().value(), "" + clientResponse.statusCode().isError(),
                                        clientResponse.headers().asHttpHeaders(), null, null)))
                        .bodyToMono(String.class)
                        .onErrorResume(throwable -> {
                            System.out.println("Error!");
                            throw new RuntimeException("Invalid token!");
                        });

            } catch (Exception e) {
                throw new RuntimeException("Invalid token!");
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
