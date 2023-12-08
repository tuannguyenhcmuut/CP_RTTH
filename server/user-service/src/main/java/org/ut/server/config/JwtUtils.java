package org.ut.server.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.ut.server.model.CustomUserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtils {
    private final String JwtSigningKey = "secret";

    public String extractUserId(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractUserID(String token) {
        return extractClaims(token, Claims::getSubject);
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


//    private Key getSigningKey() {
//
//        byte[]keyBytes= Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }


    public String generateToken(
            Map<String,Object> extraClaims,
            CustomUserDetails customUserDetails)
    {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(customUserDetails.getUserId().toString())
                //.setSubject(customUserDetails.getUserId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(SignatureAlgorithm.HS256, JwtSigningKey)
                .compact();
    }


    public String generateToken(CustomUserDetails customUserDetails){
        return generateToken(new HashMap<>(), customUserDetails);
    }

    public boolean isTokenValid(String token, CustomUserDetails userDetails){
        final String userId= extractUserId(token);

        return (userId.equals(userDetails.getUserId().toString())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
