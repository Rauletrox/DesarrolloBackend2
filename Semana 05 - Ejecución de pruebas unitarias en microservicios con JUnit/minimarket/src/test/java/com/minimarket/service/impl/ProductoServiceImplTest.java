package com.minimarket.service.impl;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
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
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Abarrotes");

        producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Arroz");
        producto.setPrecio(1000.0);
        producto.setStock(5);
        producto.setCategoria(categoria);
    }

    @Test
    void findAll_debeRetornarProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findAll();

        assertEquals(1, resultado.size());
        assertSame(producto, resultado.get(0));
        verify(productoRepository).findAll();
    }

    @Test
    void findById_debeRetornarProductoCuandoExiste() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(10L);

        assertNotNull(resultado);
        assertSame(producto, resultado);
        verify(productoRepository).findById(10L);
    }

    @Test
    void findById_debeRetornarNullCuandoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = productoService.findById(99L);

        assertNull(resultado);
        verify(productoRepository).findById(99L);
    }

    @Test
    void save_debeDelegarEnRepositorio() {
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertSame(producto, resultado);
        verify(productoRepository).save(producto);
    }

    @Test
    void deleteById_debeDelegarEnRepositorio() {
        productoService.deleteById(10L);

        verify(productoRepository).deleteById(10L);
    }

    @Test
    void findByCategoriaId_debeRetornarProductosDeCategoria() {
        when(productoRepository.findByCategoriaId(1L)).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findByCategoriaId(1L);

        assertEquals(1, resultado.size());
        assertSame(producto, resultado.get(0));
        verify(productoRepository).findByCategoriaId(1L);
    }
}
