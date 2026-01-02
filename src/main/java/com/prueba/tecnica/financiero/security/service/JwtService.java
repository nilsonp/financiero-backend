package com.prueba.tecnica.financiero.security.service;

import com.prueba.tecnica.financiero.security.dto.AuthRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Log4j2
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-hours}")
    private Long expirationHours;

    @Value("${spring.security.oauth2.client.registration.web-app.client-id}")
    private String validClientId;

    @Value("${spring.security.oauth2.client.registration.web-app.client-secret}")
    private String validClientSecret;


    public String generateToken(String clientId) {
        long expirationMillis = expirationHours * 60 * 60 * 1000;
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationMillis);

        return Jwts.builder()
                .subject(clientId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSignInKey())
                .compact();
    }

    private void autenticate(AuthRequest requestDto) {

        if(validClientId.equals(requestDto.getClientId()) &&
            validClientSecret.equals(requestDto.getClientSecret())) {
            String token = generateToken(requestDto.getClientId());

        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Token JWT inválido: {}", e.getMessage());
            return false;
        }
    }

    public String extractClientId(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
