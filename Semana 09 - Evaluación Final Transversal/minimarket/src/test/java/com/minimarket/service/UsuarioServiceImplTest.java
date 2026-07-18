package com.minimarket.service;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock private UsuarioRepository usuarioRepository;
    @InjectMocks private UsuarioServiceImpl usuarioService;

    @Test
    void debeListarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(new Usuario()));
        assertEquals(1, usuarioService.findAll().size());
    }

    @Test
    void findByIdDebeRetornarUsuarioONull() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        assertEquals(usuario, usuarioService.findById(1L).orElse(null));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(usuarioService.findById(2L).orElse(null));
    }

    @Test
    void findByUsernameDebeRetornarUsuarioONull() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        assertEquals(usuario, usuarioService.findByUsername("admin").orElse(null));
    }

    @Test
    void saveDebeDelegarAlRepositorio() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.save(any())).thenReturn(usuario);
        assertEquals(usuario, usuarioService.save(usuario));
    }

    @Test
    void deleteByIdDebeDelegarAlRepositorio() {
        usuarioService.deleteById(1L);
        verify(usuarioRepository).deleteById(1L);
    }
}
