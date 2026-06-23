package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        Categoria categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNombre("Abarrotes");

        Producto producto = new Producto();
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
    void listarMovimientosDeInventario_debeRetornarMovimientos() {
        when(inventarioService.findAll()).thenReturn(List.of(inventario));

        List<Inventario> resultado = inventarioController.listarMovimientosDeInventario();

        assertEquals(1, resultado.size());
        assertSame(inventario, resultado.get(0));
        verify(inventarioService).findAll();
    }

    @Test
    void obtenerMovimientoPorId_debeRetornarOkCuandoExiste() {
        when(inventarioService.findById(30L)).thenReturn(inventario);

        ResponseEntity<Inventario> response = inventarioController.obtenerMovimientoPorId(30L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(inventario, response.getBody());
        verify(inventarioService).findById(30L);
    }

    @Test
    void obtenerMovimientoPorId_debeRetornar404CuandoNoExiste() {
        when(inventarioService.findById(30L)).thenReturn(null);

        ResponseEntity<Inventario> response = inventarioController.obtenerMovimientoPorId(30L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(inventarioService).findById(30L);
    }

    @Test
    void registrarMovimiento_debeDelegarEnServicio() {
        when(inventarioService.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioController.registrarMovimiento(inventario);

        assertSame(inventario, resultado);
        assertSame(inventario.getProducto(), resultado.getProducto());
        verify(inventarioService).save(inventario);
    }

    @Test
    void actualizarMovimiento_debeRetornarOkCuandoExiste() {
        when(inventarioService.findById(30L)).thenReturn(inventario);
        when(inventarioService.save(any(Inventario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Inventario> response = inventarioController.actualizarMovimiento(30L, inventario);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(30L, response.getBody().getId());
        verify(inventarioService).findById(30L);
        verify(inventarioService).save(inventario);
    }

    @Test
    void actualizarMovimiento_debeRetornar404CuandoNoExiste() {
        when(inventarioService.findById(30L)).thenReturn(null);

        ResponseEntity<Inventario> response = inventarioController.actualizarMovimiento(30L, inventario);

        assertEquals(404, response.getStatusCode().value());
        verify(inventarioService).findById(30L);
        verify(inventarioService, never()).save(any());
    }

    @Test
    void eliminarMovimiento_debeRetornarNoContentCuandoExiste() {
        when(inventarioService.findById(30L)).thenReturn(inventario);

        ResponseEntity<Void> response = inventarioController.eliminarMovimiento(30L);

        assertEquals(204, response.getStatusCode().value());
        verify(inventarioService).findById(30L);
        verify(inventarioService).deleteById(30L);
    }

    @Test
    void eliminarMovimiento_debeRetornar404CuandoNoExiste() {
        when(inventarioService.findById(30L)).thenReturn(null);

        ResponseEntity<Void> response = inventarioController.eliminarMovimiento(30L);

        assertEquals(404, response.getStatusCode().value());
        verify(inventarioService).findById(30L);
        verify(inventarioService, never()).deleteById(anyLong());
    }
}
