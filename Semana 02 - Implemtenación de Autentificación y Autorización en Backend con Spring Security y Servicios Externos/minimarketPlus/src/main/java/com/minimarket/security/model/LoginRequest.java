package com.minimarket.security.model;

public class LoginRequest {

    // Cambio: modelo usado por /api/auth/login para recibir credenciales por JSON.
    private String username;

    // Cambio: la contraseña se recibe solo para autenticar; no debe devolverse en respuestas.
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
