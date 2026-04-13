package com.hrms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hrms.common.enums.Role;
import com.hrms.security.permission.RolePermissionMapper;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.access-expiration}")
    private long ACCESS_EXPIRATION;

    @Value("${jwt.refresh-expiration}")
    private long REFRESH_EXPIRATION;

    // 🔐 Generate signing key
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // =============================
    // 🔥 ACCESS TOKEN
    // =============================
    public String generateAccessToken(String email, String role) {

        List<String> permissions = RolePermissionMapper
                .getPermissions(Role.valueOf(role))
                .stream()
                .map(Enum::name)
                .toList();

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // =============================
    // 🔥 REFRESH TOKEN
    // =============================
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // =============================
    // 🔍 EXTRACT CLAIMS
    // =============================
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public List<String> extractPermissions(String token) {
        return extractAllClaims(token).get("permissions", List.class);
    }

    // =============================
    // ✅ VALIDATION
    // =============================
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}