package com.example.springsecurity.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author manhdt14
 * created in 1/27/2023 3:36 PM
 */
@Component
@Slf4j
public class JwtService {
    public static final String SECRET = "7134743777217A24432646294A404E635266556A586E3272357538782F413F44";

    public static final Integer EXPIRE_DURATION = 1000 * 60 * 30; // 30 minutes
    // defaut header
    private Header getHeader() {
        JwsHeader header = new DefaultJwsHeader();
        header.setType("JWT");
        header.setAlgorithm("HS256");
        return header;
    }

    // payload
    private Claims  setPayload(String username) {
        Claims claims = new DefaultClaims();
        claims.setIssuer(username);
        claims.setSubject(username);
        claims.setIssuedAt(new Date(System.currentTimeMillis()));
        claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION));
        return claims;
    }

    // signature
    public String generateToken(String username) {
        log.info("generate jwt ...");
        Header header = getHeader();
        Claims payload = setPayload(username);
        return createToken(header, payload);
    }

    private String createToken(Header headder, Claims payload) {
        return Jwts.builder()
                .setHeader((Map<String, Object>)headder) // header
                .setClaims(payload) // payload
                .signWith(getSignKey()).compact(); // signature (algo was set in header)
    }


    private Key getSignKey() {
        byte [] keyByte = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    @Autowired
//    public CompressionCodecResolver getAlgorithms() {
//        return new DefaultCompressionCodecResolver();
//    }

    // phan tich token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

