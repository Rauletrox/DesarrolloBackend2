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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void savePersisteProducto() {
        Producto producto = producto("Leche", 1500.0, 20);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.save(producto);

        assertNotNull(result);
        assertEquals("Leche", result.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void findByIdRetornaProductoCuandoExiste() {
        Producto producto = producto("Leche", 1500.0, 20);
        producto.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto result = productoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findByIdRetornaNullCuandoNoExiste() {
        when(productoRepository.findById(9L)).thenReturn(Optional.empty());

        Producto result = productoService.findById(9L);

        assertNull(result);
    }

    @Test
    void findByCategoriaIdDelegatesToRepository() {
        when(productoRepository.findByCategoriaId(10L)).thenReturn(List.of());

        List<Producto> result = productoService.findByCategoriaId(10L);

        assertNotNull(result);
        verify(productoRepository).findByCategoriaId(10L);
    }

    private Producto producto(String nombre, Double precio, Integer stock) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCategoria(new Categoria());
        return producto;
    }
}
