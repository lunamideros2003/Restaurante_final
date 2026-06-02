package co.ucc.pedidos.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import co.ucc.pedidos.model.Rol;
import co.ucc.pedidos.model.UsuarioModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/** Genera y valida tokens JWT con email, rol e idCliente en los claims. */
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret:PedidosUccSecretKey2026MuySeguraParaJwtTokens1234567890}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    /** Construye el token firmado que usa el frontend en Authorization: Bearer. */
    public String generateToken(UsuarioModel usuario) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claims(Map.of(
                        "rol", usuario.getRol().name(),
                        "nombre", usuario.getNombre(),
                        "idCliente", usuario.getIdCliente() != null ? usuario.getIdCliente() : ""))
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public Rol extractRol(String token) {
        String rol = parseClaims(token).get("rol", String.class);
        return Rol.valueOf(rol);
    }

    public String extractIdCliente(String token) {
        return parseClaims(token).get("idCliente", String.class);
    }

    public boolean isTokenValid(String token, String email) {
        try {
            Claims claims = parseClaims(token);
            return email.equalsIgnoreCase(claims.getSubject()) && claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }
}
