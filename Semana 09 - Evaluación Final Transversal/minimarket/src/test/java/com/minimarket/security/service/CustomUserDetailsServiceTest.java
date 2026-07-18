package com.minimarket.security.service;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @InjectMocks private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameDebeRetornarUserDetails() {
        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setPassword("secret");
        Rol rol = new Rol();
        rol.setNombre("ROLE_ADMIN");
        usuario.setRoles(Set.of(rol));
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");
        assertEquals("admin", userDetails.getUsername());
        assertEquals("secret", userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameDebeLanzarExcepcionSiNoExiste() {
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("noexiste"));
    }
}
