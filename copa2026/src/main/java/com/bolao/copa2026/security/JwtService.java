package com.bolao.copa2026.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "MinhaChaveSuperSecretaParaBolaoDaCopa2026JWT123456";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String gerarToken(Usuario usuario) {

        String role = Boolean.TRUE.equals(usuario.getAdministrador())
                ? "ADMIN"
                : "USER";

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("nome", usuario.getNome())
                .claim("email", usuario.getEmail())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public boolean tokenValido(String token) {

        try {
            extrairClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extrairClaims(String token) {

        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}