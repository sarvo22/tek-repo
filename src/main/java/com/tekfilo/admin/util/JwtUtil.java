package com.tekfilo.admin.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tekfilo.admin.config.AuthenticationConfigConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expirationTime;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
////
////    public String generate(String email) {
////        AuthForm authForm = new AuthForm();
////        authForm.setUsername(email);
////        return generate(authForm, "ACCESS");
////    }
//
//    public String generate(AuthForm auth, String type) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("id", auth.getUsername());
//        //claims.put("role", userVO.getRole());
//        return doGenerateToken(claims, auth.getUsername(), type);
//    }

    private String doGenerateToken(Map<String, Object> claims, String username, String type, String expiryTime) {
//        long expirationTimeLong;
//        if ("ACCESS".equals(type)) {
//            expirationTimeLong = Long.parseLong(expiryTime) * 1000;
//        } else {
//            expirationTimeLong = Long.parseLong(expiryTime) * 1000 * 5;
//        }
//        final Date createdDate = new Date();
//        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(createdDate)
//                .setExpiration(expirationDate)
//                .signWith(key)
//                .compact();

        return JWT.create()
                .withSubject(username)
                .withClaim("claim", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + AuthenticationConfigConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes()));
    }

    private String doGenerateToken(Map<String, Object> claims, String username, String type) {
        return doGenerateToken(claims, username, type, expirationTime);
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }


}
