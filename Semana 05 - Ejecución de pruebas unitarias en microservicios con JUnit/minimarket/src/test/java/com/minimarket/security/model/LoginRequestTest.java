package com.minimarket.security.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void loginRequest_debePoderInstanciarse() {
        LoginRequest loginRequest = new LoginRequest();

        assertNotNull(loginRequest);
    }
}
