package com.nobless.quittances.security;

import com.nobless.quittances.model.Admin;
import com.nobless.quittances.model.Proprio;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "change_this_secret_must_be_32_chars!!"; // min 32 chars pour HS256
    private final long EXPIRATION_TIME = 86400000L;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(Proprio proprio) {
        return Jwts.builder()
                .subject(proprio.getEmail())
                .claim("id", proprio.getId())
                .claim("nom", proprio.getNom())
                .claim("prenom", proprio.getPrenom())
                .claim("role", "PROPRIO")
                .claim("admin", false)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateAdminToken(Admin admin) {
        return Jwts.builder()
                .subject(admin.getLogin())
                .claim("id", admin.getId())
                .claim("role", "ADMIN")
                .claim("admin", true)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }
}