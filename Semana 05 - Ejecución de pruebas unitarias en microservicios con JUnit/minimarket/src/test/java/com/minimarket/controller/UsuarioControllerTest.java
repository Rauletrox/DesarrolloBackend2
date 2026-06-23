package com.minimarket.controller;

import com.minimarket.entity.Usuario;
import com.minimarket.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(5L);
        usuario.setUsername("cliente1");
        usuario.setPassword("secret");
    }

    @Test
    void listarUsuarios_debeRetornarLista() {
        when(usuarioService.findAll()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioController.listarUsuarios();

        assertEquals(1, resultado.size());
        assertSame(usuario, resultado.get(0));
        verify(usuarioService).findAll();
    }

    @Test
    void obtenerUsuarioPorId_debeRetornarOkCuandoExiste() {
        when(usuarioService.findById(5L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = usuarioController.obtenerUsuarioPorId(5L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(usuario, response.getBody());
        verify(usuarioService).findById(5L);
    }

    @Test
    void obtenerUsuarioPorId_debeRetornar404CuandoNoExiste() {
        when(usuarioService.findById(5L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.obtenerUsuarioPorId(5L);

        assertEquals(404, response.getStatusCode().value());
        verify(usuarioService).findById(5L);
    }

    @Test
    void guardarUsuario_debeDelegarEnServicio() {
        when(usuarioService.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioController.guardarUsuario(usuario);

        assertSame(usuario, resultado);
        verify(usuarioService).save(usuario);
    }

    @Test
    void actualizarUsuario_debeRetornarOkCuandoExiste() {
        when(usuarioService.findById(5L)).thenReturn(Optional.of(usuario));
        when(usuarioService.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Usuario> response = usuarioController.actualizarUsuario(5L, usuario);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(5L, response.getBody().getId());
        verify(usuarioService).save(usuario);
    }

    @Test
    void actualizarUsuario_debeRetornar404CuandoNoExiste() {
        when(usuarioService.findById(5L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.actualizarUsuario(5L, usuario);

        assertEquals(404, response.getStatusCode().value());
        verify(usuarioService, never()).save(any());
    }

    @Test
    void eliminarUsuario_debeRetornarNoContentCuandoExiste() {
        when(usuarioService.findById(5L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Void> response = usuarioController.eliminarUsuario(5L);

        assertEquals(204, response.getStatusCode().value());
        verify(usuarioService).deleteById(5L);
    }

    @Test
    void eliminarUsuario_debeRetornar404CuandoNoExiste() {
        when(usuarioService.findById(5L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = usuarioController.eliminarUsuario(5L);

        assertEquals(404, response.getStatusCode().value());
        verify(usuarioService, never()).deleteById(anyLong());
    }
}
