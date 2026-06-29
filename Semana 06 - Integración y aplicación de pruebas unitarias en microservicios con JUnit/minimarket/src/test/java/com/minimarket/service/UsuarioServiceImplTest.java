package com.minimarket.service;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void findAllRetornaUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario("admin")));

        List<Usuario> usuarios = usuarioService.findAll();

        assertEquals(1, usuarios.size());
    }

    @Test
    void findByIdRetornaUsuarioCuandoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario("admin")));

        Optional<Usuario> usuario = usuarioService.findById(1L);

        assertTrue(usuario.isPresent());
        assertEquals("admin", usuario.get().getUsername());
    }

    @Test
    void findByIdRetornaVacioCuandoNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Usuario> usuario = usuarioService.findById(99L);

        assertTrue(usuario.isEmpty());
    }

    @Test
    void findByUsernameRetornaUsuario() {
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario("admin")));

        Optional<Usuario> usuario = usuarioService.findByUsername("admin");

        assertTrue(usuario.isPresent());
        assertEquals("admin", usuario.get().getUsername());
    }

    @Test
    void savePersisteUsuario() {
        Usuario usuario = usuario("nuevo");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.save(usuario);

        assertNotNull(result);
        assertEquals("nuevo", result.getUsername());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deleteByIdDelegatesToRepository() {
        usuarioService.deleteById(3L);

        verify(usuarioRepository).deleteById(3L);
    }

    private Usuario usuario(String username) {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername(username);
        usuario.setPassword("encoded");
        usuario.setRoles(Set.of(new Rol("ADMIN")));
        return usuario;
    }
}
