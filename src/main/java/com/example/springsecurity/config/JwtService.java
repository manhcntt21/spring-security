package com.example.springsecurity.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
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

    public static final String SECRET_REFRESH = "645267556B58703273357638792F423F4528482B4D6251655368566D59713374";

    public static final Integer EXPIRE_DURATION = 1000 * 60 * 1; // 30 minutes

    public static final Integer TOKEN_LIFETIME = 1000 * 60 * 10; // 30 minutes
    // defaut header
    private JwsHeader getHeader() {
        JwsHeader header = new DefaultJwsHeader();
        header.setType("JWT");
        header.setAlgorithm("HS256");
        return header;
    }

    // refresh header
    private JwsHeader getHeaderRefresh() {
        JwsHeader header = new DefaultJwsHeader();
        header.setType("JWT");
        header.setAlgorithm("HS384");
        return header;
    }

    // signature
    public String generateToken(String username) {
        log.info("generate jwt ...");
        JwsHeader header = getHeader();
        Claims claims = configClaims(username);
        return createToken(header, claims);
    }

    public String generateRefreshToken(String username) {
        log.info("generate refresh token ...");
        JwsHeader header = getHeaderRefresh();
        Claims claims = configClaimsRefresh(username);
        return createRefreshToken(header, claims);
    }

    // claims
    private Claims configClaims(String username) {
        Claims claims = new DefaultClaims();
        claims.setIssuer(username);
        claims.setSubject(username);
        claims.setIssuedAt(new Date(System.currentTimeMillis()));
        claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION));
        return claims;
    }

    // claims refresh
    private Claims configClaimsRefresh(String username) {
        Claims claims = new DefaultClaims();
        claims.setIssuer(username);
        claims.setSubject(username);
        claims.setIssuedAt(new Date(System.currentTimeMillis()));
        claims.setExpiration(new Date(System.currentTimeMillis() + TOKEN_LIFETIME));
        return claims;
    }

    private String createToken(JwsHeader header, Claims claims) {
        return Jwts.builder()
                .setHeader((Map<String, Object>)header) // header
//                .setPayload(claims.toString()) // payload
                .setClaims(claims) // payload
                .signWith(getSignKey(1), SignatureAlgorithm.HS256).compact(); // signature must be explicitly specified here
    }

    private String createRefreshToken(JwsHeader header, Claims claims) {
        return Jwts.builder()
                .setHeader((Map<String, Object>)header) // header
//                .setPayload(claims.toString()) // payload
                .setClaims(claims) // payload
                .signWith(getSignKey(0), SignatureAlgorithm.HS384).compact(); // signature must be explicitly specified here
    }

    /**
     * 1: get access key
     * 0: get refresh key
     * hiểu là key đã được encode theo Base64, nên đoạn này phải decode nó về UTF-8 (default của nó)
     * trả về một SecretKey để sign trong signWith
     * @param type
     * @return
     */
    private Key getSignKey(int type) {
        byte [] keyByte = Decoders.BASE64.decode(type == 1 ? SECRET : SECRET_REFRESH);
        return Keys.hmacShaKeyFor(keyByte);
    }


    public String extractUsername(String jwtToken, int type) {
        return extractClaim(jwtToken, Claims::getSubject, type);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, int type) {
        final Claims claims = extractAllClaims(token, type);
        return claimsResolver.apply(claims);
    }


    // phan tich token
    private Claims extractAllClaims(String token,int type) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey(type))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token, int type) {
        return extractExpiration(token, type).before(new Date());
    }

    public Date extractExpiration(String token, int type) {
        return extractClaim(token, Claims::getExpiration, type);
    }

    /**
     * chỗ này thật ra chỉ cần truyền vào name đã trích xuất ở JwtAuFilter, nhưng do cần check hạn nữa nên vẫn phải truyền
     * token vào nên hàm extractUsername được tính 2 lần, khắc phục sau
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails, int type) {
        final String username = extractUsername(token, type);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, type));
    }

    public Boolean checkRefreshTokenExpired(String token, int type) {
        return !isTokenExpired(token, type);
    }

    public String generateNewAccessToken(String token, String username, int type) {
        try {
            if(checkRefreshTokenExpired(token, type)) {
                // nếu refresh còn hạn
                return generateToken(username);
            }
        } catch (Exception e) {
            log.error("invalid refresh token");
//            e.printStackTrace();
        }
        return "invalid refresh token";
    }
}

