package org.ut.server.common.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class CommonJwtUtils {
    private final String JwtSigningKey = "secret";

//    public String extractUserId(String token) {
//        return extractClaims(token, Claims::getSubject);
//    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JwtSigningKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", String.class);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsTResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsTResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(JwtSigningKey)
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean isTokenValid(String token){
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

}
