package com.minimarket.service.impl;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Producto producto;
    private Inventario inventario;

    @BeforeEach
    void setUp() {
        Categoria categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNombre("Abarrotes");

        producto = new Producto();
        producto.setId(100L);
        producto.setNombre("Arroz");
        producto.setPrecio(1290.0);
        producto.setStock(15);
        producto.setCategoria(categoria);

        inventario = new Inventario();
        inventario.setId(30L);
        inventario.setProducto(producto);
        inventario.setCantidad(5);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());
    }

    @Test
    void findAll_debeRetornarMovimientos() {
        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        List<Inventario> resultado = inventarioService.findAll();

        assertEquals(1, resultado.size());
        assertSame(inventario, resultado.get(0));
        verify(inventarioRepository).findAll();
    }

    @Test
    void findById_debeRetornarMovimientoCuandoExiste() {
        when(inventarioRepository.findById(30L)).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.findById(30L);

        assertNotNull(resultado);
        assertSame(inventario, resultado);
        assertSame(producto, resultado.getProducto());
        verify(inventarioRepository).findById(30L);
    }

    @Test
    void findById_debeRetornarNullCuandoNoExiste() {
        when(inventarioRepository.findById(404L)).thenReturn(Optional.empty());

        Inventario resultado = inventarioService.findById(404L);

        assertNull(resultado);
        verify(inventarioRepository).findById(404L);
    }

    @Test
    void save_debePreservarRelacionProducto() {
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.save(inventario);

        assertSame(inventario, resultado);
        assertSame(producto, resultado.getProducto());
        assertNotNull(resultado.getTipoMovimiento());
        assertNotNull(resultado.getCantidad());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void deleteById_debeDelegarEnRepositorio() {
        inventarioService.deleteById(30L);

        verify(inventarioRepository).deleteById(30L);
    }

    @Test
    void findByProductoId_debeRetornarMovimientosDelProducto() {
        when(inventarioRepository.findByProductoId(100L)).thenReturn(List.of(inventario));

        List<Inventario> resultado = inventarioService.findByProductoId(100L);

        assertEquals(1, resultado.size());
        assertSame(inventario, resultado.get(0));
        verify(inventarioRepository).findByProductoId(100L);
    }
}
