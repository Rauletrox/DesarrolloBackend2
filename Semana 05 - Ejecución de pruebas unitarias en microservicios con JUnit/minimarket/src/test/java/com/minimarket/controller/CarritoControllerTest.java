package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.service.CarritoService;
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
class CarritoControllerTest {

    @Mock
    private CarritoService carritoService;

    @InjectMocks
    private CarritoController carritoController;

    private Carrito carrito;

    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("cliente1");
        usuario.setPassword("secret");

        Categoria categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNombre("Abarrotes");

        Producto producto = new Producto();
        producto.setId(100L);
        producto.setNombre("Arroz");
        producto.setPrecio(1290.0);
        producto.setStock(15);
        producto.setCategoria(categoria);

        carrito = new Carrito();
        carrito.setId(20L);
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
        carrito.setCantidad(3);
    }

    @Test
    void listarCarrito_debeRetornarLosElementosDelServicio() {
        when(carritoService.findAll()).thenReturn(List.of(carrito));

        List<Carrito> resultado = carritoController.listarCarrito();

        assertEquals(1, resultado.size());
        assertSame(carrito, resultado.get(0));
        verify(carritoService).findAll();
    }

    @Test
    void obtenerCarritoPorId_debeRetornarOkCuandoExiste() {
        when(carritoService.findById(20L)).thenReturn(carrito);

        ResponseEntity<Carrito> response = carritoController.obtenerCarritoPorId(20L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(carrito, response.getBody());
        verify(carritoService).findById(20L);
    }

    @Test
    void obtenerCarritoPorId_debeRetornar404CuandoNoExiste() {
        when(carritoService.findById(20L)).thenReturn(null);

        ResponseEntity<Carrito> response = carritoController.obtenerCarritoPorId(20L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(carritoService).findById(20L);
    }

    @Test
    void agregarProductoAlCarrito_debeDelegarEnServicio() {
        when(carritoService.save(carrito)).thenReturn(carrito);

        Carrito resultado = carritoController.agregarProductoAlCarrito(carrito);

        assertSame(carrito, resultado);
        assertSame(carrito.getUsuario(), resultado.getUsuario());
        assertSame(carrito.getProducto(), resultado.getProducto());
        verify(carritoService).save(carrito);
    }

    @Test
    void actualizarCarrito_debeRetornarOkCuandoExiste() {
        when(carritoService.findById(20L)).thenReturn(carrito);
        when(carritoService.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Carrito> response = carritoController.actualizarCarrito(20L, carrito);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(20L, response.getBody().getId());
        verify(carritoService).findById(20L);
        verify(carritoService).save(carrito);
    }

    @Test
    void actualizarCarrito_debeRetornar404CuandoNoExiste() {
        when(carritoService.findById(20L)).thenReturn(null);

        ResponseEntity<Carrito> response = carritoController.actualizarCarrito(20L, carrito);

        assertEquals(404, response.getStatusCode().value());
        verify(carritoService).findById(20L);
        verify(carritoService, never()).save(any());
    }

    @Test
    void eliminarProductoDelCarrito_debeRetornarNoContentCuandoExiste() {
        when(carritoService.findById(20L)).thenReturn(carrito);

        ResponseEntity<Void> response = carritoController.eliminarProductoDelCarrito(20L);

        assertEquals(204, response.getStatusCode().value());
        verify(carritoService).findById(20L);
        verify(carritoService).deleteById(20L);
    }

    @Test
    void eliminarProductoDelCarrito_debeRetornar404CuandoNoExiste() {
        when(carritoService.findById(20L)).thenReturn(null);

        ResponseEntity<Void> response = carritoController.eliminarProductoDelCarrito(20L);

        assertEquals(404, response.getStatusCode().value());
        verify(carritoService).findById(20L);
        verify(carritoService, never()).deleteById(anyLong());
    }
}
