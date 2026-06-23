package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
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
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

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
    void listarProductos_debeRetornarLista() {
        when(productoService.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoController.listarProductos();

        assertEquals(1, resultado.size());
        assertSame(producto, resultado.get(0));
        verify(productoService).findAll();
    }

    @Test
    void obtenerProductoPorId_debeRetornarOkCuandoExiste() {
        when(productoService.findById(10L)).thenReturn(producto);

        ResponseEntity<Producto> response = productoController.obtenerProductoPorId(10L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(producto, response.getBody());
        verify(productoService).findById(10L);
    }

    @Test
    void obtenerProductoPorId_debeRetornar404CuandoNoExiste() {
        when(productoService.findById(10L)).thenReturn(null);

        ResponseEntity<Producto> response = productoController.obtenerProductoPorId(10L);

        assertEquals(404, response.getStatusCode().value());
        verify(productoService).findById(10L);
    }

    @Test
    void guardarProducto_debeDelegarEnServicio() {
        when(productoService.save(producto)).thenReturn(producto);

        Producto resultado = productoController.guardarProducto(producto);

        assertSame(producto, resultado);
        verify(productoService).save(producto);
    }

    @Test
    void actualizarProducto_debeRetornarOkCuandoExiste() {
        when(productoService.findById(10L)).thenReturn(producto);
        when(productoService.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Producto> response = productoController.actualizarProducto(10L, producto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(10L, response.getBody().getId());
        verify(productoService).save(producto);
    }

    @Test
    void actualizarProducto_debeRetornar404CuandoNoExiste() {
        when(productoService.findById(10L)).thenReturn(null);

        ResponseEntity<Producto> response = productoController.actualizarProducto(10L, producto);

        assertEquals(404, response.getStatusCode().value());
        verify(productoService, never()).save(any());
    }

    @Test
    void eliminarProducto_debeRetornarNoContentCuandoExiste() {
        when(productoService.findById(10L)).thenReturn(producto);

        ResponseEntity<Void> response = productoController.eliminarProducto(10L);

        assertEquals(204, response.getStatusCode().value());
        verify(productoService).deleteById(10L);
    }

    @Test
    void eliminarProducto_debeRetornar404CuandoNoExiste() {
        when(productoService.findById(10L)).thenReturn(null);

        ResponseEntity<Void> response = productoController.eliminarProducto(10L);

        assertEquals(404, response.getStatusCode().value());
        verify(productoService, never()).deleteById(anyLong());
    }
}
