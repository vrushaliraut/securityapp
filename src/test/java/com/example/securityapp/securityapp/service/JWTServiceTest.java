package com.example.securityapp.securityapp.service;
import com.example.securityapp.securityapp.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;

    // Constants for testing
    private final long EXPIRATION_MILLIS = 900000; // 15 min
    private final String TEST_SECRET = "my-test-secret";

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        jwtService.jwtSecret = TEST_SECRET;
        jwtService.jwtExpiration = EXPIRATION_MILLIS;
    }

    @Test
    void shouldGenerateJWTTokenContainingUsernameAndRoles() {
        UserEntity user = new UserEntity("testuser", "irrelevantHash", Set.of("USER", "ADMIN"));
        String token = jwtService.generateToken(user);

        assertThat(token).isNotBlank();

        Claims claims = Jwts.parser()
                .setSigningKey(TEST_SECRET)
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("testuser");
        assertThat((Set<String>) claims.get("roles")).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    void shouldSetTokenExpirationCorrectly() {
        UserEntity user = new UserEntity("testuser", "irrelevantHash", Set.of("USER"));
        long before = System.currentTimeMillis();
        String token = jwtService.generateToken(user);
        long after = System.currentTimeMillis();

        Claims claims = Jwts.parser()
                .setSigningKey(TEST_SECRET)
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();
        assertThat(expiration.getTime()).isBetween(before + EXPIRATION_MILLIS, after + EXPIRATION_MILLIS + 100);
    }

    @Test
    void shouldThrowExceptionIfTokenTampered() {
        UserEntity user = new UserEntity("testuser", "irrelevantHash", Set.of("USER"));
        String token = jwtService.generateToken(user);

        // Use wrong secret
        assertThatThrownBy(() ->
                Jwts.parser()
                        .setSigningKey("wrong-secret")
                        .parseClaimsJws(token)
        ).isInstanceOf(SignatureException.class);
    }
}