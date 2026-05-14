package com.cafetito.security;

import com.cafetito.entity.Rol;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class JwtUtil {

    private static final int MIN_KEY_BYTES = 32;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < MIN_KEY_BYTES) {
            keyBytes = sha256(keyBytes);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static byte[] sha256(byte[] input) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    // Cambia la firma del para recibir el idUsuario
    public String generateToken(Long idUsuario, String username, Rol rol, String nitAgricultor) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        var builder = Jwts.builder()
                .subject(username)
                .claim("idUsuario", idUsuario) // ✅ AGREGAMOS EL ID AQUÍ
                .claim("rol", rol.name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey());

        if (nitAgricultor != null && !nitAgricultor.isBlank()) {
            builder.claim("nitAgricultor", nitAgricultor);
        }
        return builder.compact();
    }

    // Agrega este para extraerlo fácilmente después
    public Long extractIdUsuario(String token) {
        Object id = extractAllClaims(token).get("idUsuario");
        return id != null ? Long.valueOf(id.toString()) : null;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRol(String token) {
        return extractAllClaims(token).get("rol", String.class);
    }

    public String extractNitAgricultor(String token) {
        try {
            Object claim = extractAllClaims(token).get("nitAgricultor");
            return claim != null ? claim.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
