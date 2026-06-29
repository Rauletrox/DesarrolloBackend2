package com.minimarket.service;

import com.minimarket.entity.Categoria;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.service.impl.CategoriaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Test
    void saveGuardaCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Bebidas");
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria result = categoriaService.save(categoria);

        assertNotNull(result);
        assertEquals("Bebidas", result.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void findByIdRetornaCategoriaCuandoExiste() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria result = categoriaService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findByIdRetornaNullCuandoNoExiste() {
        when(categoriaRepository.findById(9L)).thenReturn(Optional.empty());

        Categoria result = categoriaService.findById(9L);

        assertNull(result);
    }

    @Test
    void findAllRetornaCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(new Categoria()));

        List<Categoria> categorias = categoriaService.findAll();

        assertNotNull(categorias);
        assertEquals(1, categorias.size());
    }
}
