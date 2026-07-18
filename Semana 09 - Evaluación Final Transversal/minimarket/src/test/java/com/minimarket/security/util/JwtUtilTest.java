package com.minimarket.security.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "minimarket-plus-secret-key-minimarket-plus-secret-key");
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3600000L);
    }

    @Test
    void debeGenerarYValidarToken() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                "admin123",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String token = jwtUtil.generateToken(authentication);

        assertTrue(jwtUtil.validateToken(token));
        assertEquals("admin", jwtUtil.getUsernameFromToken(token));
    }

    @Test
    void validateTokenDebeRetornarFalseParaTokenInvalido() {
        assertFalse(jwtUtil.validateToken("token-invalido"));
    }
}
