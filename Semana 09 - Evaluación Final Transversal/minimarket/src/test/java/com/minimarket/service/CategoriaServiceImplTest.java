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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock private CategoriaRepository categoriaRepository;
    @InjectMocks private CategoriaServiceImpl categoriaService;

    @Test
    void debeListarCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(new Categoria()));
        assertEquals(1, categoriaService.findAll().size());
    }

    @Test
    void findByIdDebeRetornarCategoriaONull() {
        Categoria categoria = new Categoria();
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        assertEquals(categoria, categoriaService.findById(1L));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(categoriaService.findById(2L));
    }

    @Test
    void saveDebeDelegarAlRepositorio() {
        Categoria categoria = new Categoria();
        when(categoriaRepository.save(any())).thenReturn(categoria);
        assertEquals(categoria, categoriaService.save(categoria));
    }

    @Test
    void deleteByIdDebeDelegarAlRepositorio() {
        categoriaService.deleteById(1L);
        verify(categoriaRepository).deleteById(1L);
    }
}
