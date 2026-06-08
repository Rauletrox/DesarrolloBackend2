package com.minimarket.security.model;

import com.minimarket.entity.Usuario;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (usuario.getRoles() == null) {
            return Collections.emptyList();
        }
        return usuario.getRoles().stream()
                // Se cambio: Spring Security espera el prefijo ROLE_ cuando se usan hasRole/hasAnyRole.
                .map(rol -> new SimpleGrantedAuthority(normalizarRol(rol.getNombre())))
                .collect(Collectors.toList());
    }

    private String normalizarRol(String nombreRol) {
        // Se cambio: permite guardar roles como CLIENTE o ROLE_CLIENTE sin romper la autorizacion.
        return nombreRol.startsWith("ROLE_") ? nombreRol : "ROLE_" + nombreRol;
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
