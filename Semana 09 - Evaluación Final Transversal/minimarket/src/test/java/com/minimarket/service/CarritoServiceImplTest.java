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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock private CarritoRepository carritoRepository;
    @InjectMocks private CarritoServiceImpl carritoService;

    @Test
    void debeListarCarritos() {
        when(carritoRepository.findAll()).thenReturn(List.of(new Carrito()));
        assertEquals(1, carritoService.findAll().size());
    }

    @Test
    void findByIdDebeRetornarCarritoONull() {
        Carrito carrito = new Carrito();
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        assertEquals(carrito, carritoService.findById(1L));
        when(carritoRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(carritoService.findById(2L));
    }

    @Test
    void saveDebeDelegarAlRepositorio() {
        Carrito carrito = new Carrito();
        when(carritoRepository.save(any())).thenReturn(carrito);
        assertEquals(carrito, carritoService.save(carrito));
    }

    @Test
    void deleteByIdDebeDelegarAlRepositorio() {
        carritoService.deleteById(1L);
        verify(carritoRepository).deleteById(1L);
    }

    @Test
    void findByUsuarioIdDebeDelegarAlRepositorio() {
        when(carritoRepository.findByUsuarioId(1L)).thenReturn(List.of(new Carrito()));
        assertEquals(1, carritoService.findByUsuarioId(1L).size());
    }
}
