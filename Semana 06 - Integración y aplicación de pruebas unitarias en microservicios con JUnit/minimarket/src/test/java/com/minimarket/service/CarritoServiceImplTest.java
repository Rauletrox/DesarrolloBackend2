package com.minimarket.service;

import com.minimarket.entity.Carrito;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.impl.CarritoServiceImpl;
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
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    @Test
    void saveGuardaCarrito() {
        Carrito carrito = new Carrito();
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        Carrito result = carritoService.save(carrito);

        assertNotNull(result);
        verify(carritoRepository).save(carrito);
    }

    @Test
    void findByIdRetornaCarritoCuandoExiste() {
        Carrito carrito = new Carrito();
        carrito.setId(1L);
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        Carrito result = carritoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findByIdRetornaNullCuandoNoExiste() {
        when(carritoRepository.findById(9L)).thenReturn(Optional.empty());

        Carrito result = carritoService.findById(9L);

        assertNull(result);
    }

    @Test
    void findByUsuarioIdDelegatesToRepository() {
        when(carritoRepository.findByUsuarioId(12L)).thenReturn(List.of());

        List<Carrito> result = carritoService.findByUsuarioId(12L);

        assertNotNull(result);
        verify(carritoRepository).findByUsuarioId(12L);
    }
}
