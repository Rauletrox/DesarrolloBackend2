package com.minimarket.service.impl;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
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
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Usuario usuario;
    private Producto producto;
    private Carrito carrito;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("cliente1");
        usuario.setPassword("secret");

        Categoria categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNombre("Abarrotes");

        producto = new Producto();
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
    void findAll_debeRetornarTodosLosCarritos() {
        when(carritoRepository.findAll()).thenReturn(List.of(carrito));

        List<Carrito> resultado = carritoService.findAll();

        assertEquals(1, resultado.size());
        assertSame(carrito, resultado.get(0));
        verify(carritoRepository).findAll();
    }

    @Test
    void findById_debeRetornarCarritoCuandoExiste() {
        when(carritoRepository.findById(20L)).thenReturn(Optional.of(carrito));

        Carrito resultado = carritoService.findById(20L);

        assertNotNull(resultado);
        assertSame(carrito, resultado);
        assertSame(usuario, resultado.getUsuario());
        assertSame(producto, resultado.getProducto());
        verify(carritoRepository).findById(20L);
    }

    @Test
    void findById_debeRetornarNullCuandoNoExiste() {
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        Carrito resultado = carritoService.findById(99L);

        assertNull(resultado);
        verify(carritoRepository).findById(99L);
    }

    @Test
    void save_debePreservarRelacionUsuarioYProducto() {
        when(carritoRepository.save(carrito)).thenReturn(carrito);

        Carrito resultado = carritoService.save(carrito);

        assertSame(carrito, resultado);
        assertSame(usuario, resultado.getUsuario());
        assertSame(producto, resultado.getProducto());
        assertTrue(resultado.getProducto().getStock() >= resultado.getCantidad());
        verify(carritoRepository).save(carrito);
    }

    @Test
    void deleteById_debeDelegarEnRepositorio() {
        carritoService.deleteById(20L);

        verify(carritoRepository).deleteById(20L);
    }

    @Test
    void findByUsuarioId_debeRetornarCarritosDelUsuario() {
        when(carritoRepository.findByUsuarioId(1L)).thenReturn(List.of(carrito));

        List<Carrito> resultado = carritoService.findByUsuarioId(1L);

        assertEquals(1, resultado.size());
        assertSame(carrito, resultado.get(0));
        verify(carritoRepository).findByUsuarioId(1L);
    }
}
