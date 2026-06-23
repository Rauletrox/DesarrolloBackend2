package com.minimarket.security.model;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void customUserDetails_debeMapearUsuarioYRoles() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("cliente1");
        usuario.setPassword("secret");
        usuario.setRoles(Set.of(new Rol("ADMIN"), new Rol("USER")));

        CustomUserDetails userDetails = new CustomUserDetails(usuario);

        assertEquals("cliente1", userDetails.getUsername());
        assertEquals("secret", userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch("ADMIN"::equals));
        assertTrue(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch("USER"::equals));
    }
}
