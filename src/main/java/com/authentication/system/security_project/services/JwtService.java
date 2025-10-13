package com.authentication.system.security_project.services;

import com.authentication.system.security_project.utils.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.KeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private Utils utils;

    public JwtService(Utils utils) {
        this.utils = utils;
    }

    public SecretKey generateKey() throws KeyException {
        byte[] byteStream = Decoders.BASE64.decode(utils.getJwt().getSecret());
        return Keys.hmacShaKeyFor(byteStream);
    }

    public String generateToken(String username) {
        Map<String, String> claims = Map.of("Algorithm", "hmacShaKeyFor");
        return Jwts.builder()
                   .subject(username)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis() + 60 * 1000L * utils.getJwt().getExpiry()))
                   .signWith(generateKey())
                   .claims(claims)
                   .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .verifyWith(generateKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();

    }

    public boolean isExpired(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public boolean validateJwt(UserDetails userDetails, String token) {
        Claims claims = extractAllClaims(token);
        return !isExpired(token) && claims.getSubject().equals(userDetails.getUsername());
    }
}
