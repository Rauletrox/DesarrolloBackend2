package com.minimarket.security;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.model.CustomUserDetails;
import com.minimarket.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameRetornaDetallesDeUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("cajero1");
        usuario.setPassword("encoded");
        usuario.setRoles(Set.of(new Rol("CAJERO")));
        when(usuarioRepository.findByUsername("cajero1")).thenReturn(Optional.of(usuario));

        UserDetails result = customUserDetailsService.loadUserByUsername("cajero1");

        assertInstanceOf(CustomUserDetails.class, result);
        assertEquals("cajero1", result.getUsername());
        assertEquals(1, result.getAuthorities().size());
    }

    @Test
    void loadUserByUsernameLanzaExcepcionSiNoExiste() {
        when(usuarioRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("inexistente"));
    }
}
