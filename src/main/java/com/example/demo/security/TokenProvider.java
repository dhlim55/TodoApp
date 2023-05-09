package com.example.demo.security;

import com.example.demo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "asdfalkfgakerflallk";

    public String create(UserEntity userEntity) {
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));
        /*
        {
            "alg":"HS512"
        }.
        {
            "sub":"",
            "iss":"demo app",
            "iat":1595733657,
            "exp":1595733657
        }.
        */
         return Jwts.builder()
                 .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                 .setSubject(userEntity.getId())
                 .setIssuer("demo app")
                 .setIssuedAt(new Date())
                 .setExpiration(expiryDate)
                 .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
