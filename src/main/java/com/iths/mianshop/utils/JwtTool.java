package com.iths.mianshop.utils;


import com.iths.mianshop.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtTool {

    private final JwtProperties jwtProperties;
    private final KeyPair keyPair; // RSA密钥对

    public JwtTool(KeyPair keyPair, JwtProperties jwtProperties) {
        this.keyPair = keyPair;
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(String subject, Map<String, Object> claims) {

        long expiration = jwtProperties.getTokenTTL().toMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
    }


    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(keyPair.getPublic())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return parseToken(token).getSubject();
    }

}
