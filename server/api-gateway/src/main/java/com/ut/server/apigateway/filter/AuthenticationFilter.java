package com.ut.server.apigateway.filter;


import com.ut.server.apigateway.config.GatewayInterface;
import com.ut.server.apigateway.util.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    private WebClient.Builder webClientBuilder;

    //    @Autowired
    @Autowired
    private RestTemplate template;

    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder=webClientBuilder;
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
//                    //REST call to AUTH service
//                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
                    HttpHeaders headers = new HttpHeaders();
//                    headers.setContentType(MediaType.APPLICATION_JSON);
//                    headers.set("Authorization", authHeader);
                    headers.setBearerAuth(authHeader);
                    webClientBuilder.build()
                            .method(HttpMethod.GET)
                            .uri("http://user-service/auth/validate")
                            .header(HttpHeaders.AUTHORIZATION, authHeader)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

//                    template.exchange("http://user-service/auth/validate", HttpMethod.GET, new HttpEntity<>(headers), String.class);
//                    Reponse gatewayInterface.validateToken(authHeader);
//                    jwtUtils.isTokenValid(authHeader, null);
//"http://user-service/auth/validate",  new HttpEntity<Object>(headers), String.class
//                    jwtUtil.validateToken(authHeader);

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
