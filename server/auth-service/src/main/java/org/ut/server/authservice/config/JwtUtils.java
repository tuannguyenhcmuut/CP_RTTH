package org.ut.server.authservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.ut.server.authservice.model.CustomUserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${oms.app.jwtSecret}")
    private String JwtSigningKey;
    @Value("${oms.app.jwtExpirationMs}")
    private Long jwtDurationMs;

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    //    public String extractUserID(String token) {
//        return extractClaims(token, Claims::getSubject);
//    }
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


    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(JwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(
            Map<String, Object> extraClaims,
            CustomUserDetails customUserDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(customUserDetails.getUsername())
                .claim("userId", customUserDetails.getUserId())
                //.setSubject(customUserDetails.getUserId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtDurationMs))
                .signWith(SignatureAlgorithm.HS256, JwtSigningKey)
                .compact();
    }


    public String generateToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, customUserDetails);
    }

//    public boolean isTokenValid(String token, CustomUserDetails userDetails){
//        final String userId= extractUserId(token);
//
//        return (userId.equals(userDetails.getUserId().toString())) && !isTokenExpired(token);
//    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
