package com.minimarket.service.impl;

import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(5L);
        usuario.setUsername("cliente1");
        usuario.setPassword("secret");
    }

    @Test
    void findAll_debeRetornarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.findAll();

        assertEquals(1, resultado.size());
        assertSame(usuario, resultado.get(0));
        verify(usuarioRepository).findAll();
    }

    @Test
    void findById_debeRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.findById(5L);

        assertTrue(resultado.isPresent());
        assertSame(usuario, resultado.get());
        verify(usuarioRepository).findById(5L);
    }

    @Test
    void findById_debeRetornarOptionalVacioCuandoNoExiste() {
        when(usuarioRepository.findById(9L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.findById(9L);

        assertTrue(resultado.isEmpty());
        verify(usuarioRepository).findById(9L);
    }

    @Test
    void findByUsername_debeRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findByUsername("cliente1")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.findByUsername("cliente1");

        assertTrue(resultado.isPresent());
        assertSame(usuario, resultado.get());
        verify(usuarioRepository).findByUsername("cliente1");
    }

    @Test
    void save_debeDelegarEnRepositorio() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.save(usuario);

        assertSame(usuario, resultado);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deleteById_debeDelegarEnRepositorio() {
        usuarioService.deleteById(5L);

        verify(usuarioRepository).deleteById(5L);
    }
}
