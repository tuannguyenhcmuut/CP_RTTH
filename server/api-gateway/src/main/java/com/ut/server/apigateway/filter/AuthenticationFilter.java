package com.ut.server.apigateway.filter;


import com.ut.server.apigateway.exception.ErrorResponse;
import com.ut.server.apigateway.util.JwtUtils;
import com.ut.server.apigateway.util.RouteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.HandlerInterceptor;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> implements HandlerInterceptor {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtils jwtUtils;

//    @Autowired
//    @Qualifier("handlerExceptionResolver")
//    private HandlerExceptionResolver exceptionResolver;

    @Autowired
    public AuthenticationFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
         return ((exchange, chain) -> {
            if (validator.isSecured.test( exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    String username = null;
                    String userId = null;
                    if (jwtUtils.isTokenValid(authHeader)) {
                        username = jwtUtils.extractUsername(authHeader);
                        userId = jwtUtils.extractUserId(authHeader);
                        log.info(jwtUtils.extractUserId(authHeader));
                    }

                    log.info("Token is valid with username: {}", username);
                    System.out.println("Token is valid with userId: "+ userId);

                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("userId", userId)
                            .build();
                    // Replace the existing request in the exchange with the new one
                    exchange = exchange.mutate().request(request).build();
                }
                catch (Exception e) {
                    log.error("Exception on Gateway Filter: " + e.toString());
                    return Mono.error(e);
                }
            }

            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, ErrorResponse errorResponse) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }


    public static class Config {

    }
}
