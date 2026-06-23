package com.minimarket.service.impl;

import com.minimarket.entity.Categoria;
import com.minimarket.repository.CategoriaRepository;
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
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombre("Limpieza");
    }

    @Test
    void findAll_debeRetornarCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> resultado = categoriaService.findAll();

        assertEquals(1, resultado.size());
        assertSame(categoria, resultado.get(0));
        verify(categoriaRepository).findAll();
    }

    @Test
    void findById_debeRetornarCategoriaCuandoExiste() {
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.findById(2L);

        assertNotNull(resultado);
        assertSame(categoria, resultado);
        verify(categoriaRepository).findById(2L);
    }

    @Test
    void findById_debeRetornarNullCuandoNoExiste() {
        when(categoriaRepository.findById(9L)).thenReturn(Optional.empty());

        Categoria resultado = categoriaService.findById(9L);

        assertNull(resultado);
        verify(categoriaRepository).findById(9L);
    }

    @Test
    void save_debeDelegarEnRepositorio() {
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaService.save(categoria);

        assertSame(categoria, resultado);
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void deleteById_debeDelegarEnRepositorio() {
        categoriaService.deleteById(2L);

        verify(categoriaRepository).deleteById(2L);
    }
}
