package com.minimarket.security.model;

import java.util.List;

public class JwtResponse {

    private final String token;
    private final String tipo;
    private final String username;
    private final List<String> roles;

    public JwtResponse(String token, String username, List<String> roles) {
        this.token = token;
        this.tipo = "Bearer";
        this.username = username;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
