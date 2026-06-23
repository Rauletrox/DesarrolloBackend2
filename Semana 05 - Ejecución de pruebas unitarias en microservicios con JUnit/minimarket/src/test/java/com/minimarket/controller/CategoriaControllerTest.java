package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombre("Limpieza");
    }

    @Test
    void listarCategorias_debeRetornarLista() {
        when(categoriaService.findAll()).thenReturn(List.of(categoria));

        List<Categoria> resultado = categoriaController.listarCategorias();

        assertEquals(1, resultado.size());
        assertSame(categoria, resultado.get(0));
        verify(categoriaService).findAll();
    }

    @Test
    void obtenerCategoriaPorId_debeRetornarOkCuandoExiste() {
        when(categoriaService.findById(2L)).thenReturn(categoria);

        ResponseEntity<Categoria> response = categoriaController.obtenerCategoriaPorId(2L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(categoria, response.getBody());
        verify(categoriaService).findById(2L);
    }

    @Test
    void obtenerCategoriaPorId_debeRetornar404CuandoNoExiste() {
        when(categoriaService.findById(2L)).thenReturn(null);

        ResponseEntity<Categoria> response = categoriaController.obtenerCategoriaPorId(2L);

        assertEquals(404, response.getStatusCode().value());
        verify(categoriaService).findById(2L);
    }

    @Test
    void guardarCategoria_debeDelegarEnServicio() {
        when(categoriaService.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaController.guardarCategoria(categoria);

        assertSame(categoria, resultado);
        verify(categoriaService).save(categoria);
    }

    @Test
    void actualizarCategoria_debeRetornarOkCuandoExiste() {
        when(categoriaService.findById(2L)).thenReturn(categoria);
        when(categoriaService.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Categoria> response = categoriaController.actualizarCategoria(2L, categoria);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2L, response.getBody().getId());
        verify(categoriaService).save(categoria);
    }

    @Test
    void actualizarCategoria_debeRetornar404CuandoNoExiste() {
        when(categoriaService.findById(2L)).thenReturn(null);

        ResponseEntity<Categoria> response = categoriaController.actualizarCategoria(2L, categoria);

        assertEquals(404, response.getStatusCode().value());
        verify(categoriaService, never()).save(any());
    }

    @Test
    void eliminarCategoria_debeRetornarNoContentCuandoExiste() {
        when(categoriaService.findById(2L)).thenReturn(categoria);

        ResponseEntity<Void> response = categoriaController.eliminarCategoria(2L);

        assertEquals(204, response.getStatusCode().value());
        verify(categoriaService).deleteById(2L);
    }

    @Test
    void eliminarCategoria_debeRetornar404CuandoNoExiste() {
        when(categoriaService.findById(2L)).thenReturn(null);

        ResponseEntity<Void> response = categoriaController.eliminarCategoria(2L);

        assertEquals(404, response.getStatusCode().value());
        verify(categoriaService, never()).deleteById(anyLong());
    }
}
