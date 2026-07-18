package com.minimarket.service;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.ProductoServiceImpl;
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
class ProductoServiceImplTest {

    @Mock private ProductoRepository productoRepository;
    @InjectMocks private ProductoServiceImpl productoService;

    @Test
    void findAllDebeDelegarAlRepositorio() {
        when(productoRepository.findAll()).thenReturn(List.of(new Producto()));
        assertEquals(1, productoService.findAll().size());
    }

    @Test
    void findByIdDebeRetornarProductoONull() {
        Producto producto = new Producto();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        assertEquals(producto, productoService.findById(1L));
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(productoService.findById(2L));
    }

    @Test
    void saveDebeDelegarAlRepositorio() {
        Producto producto = new Producto();
        when(productoRepository.save(any())).thenReturn(producto);
        assertEquals(producto, productoService.save(producto));
    }

    @Test
    void deleteByIdDebeDelegarAlRepositorio() {
        productoService.deleteById(1L);
        verify(productoRepository).deleteById(1L);
    }

    @Test
    void findByCategoriaIdDebeDelegarAlRepositorio() {
        when(productoRepository.findByCategoriaId(1L)).thenReturn(List.of(new Producto()));
        assertEquals(1, productoService.findByCategoriaId(1L).size());
    }
}
