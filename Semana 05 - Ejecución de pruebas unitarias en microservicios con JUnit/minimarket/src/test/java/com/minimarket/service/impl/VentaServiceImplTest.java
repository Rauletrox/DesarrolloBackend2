package com.minimarket.service.impl;

import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
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
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private Venta venta;

    @BeforeEach
    void setUp() {
        venta = new Venta();
        venta.setId(7L);
        venta.setFecha(new Date());
    }

    @Test
    void findAll_debeRetornarVentas() {
        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<Venta> resultado = ventaService.findAll();

        assertEquals(1, resultado.size());
        assertSame(venta, resultado.get(0));
        verify(ventaRepository).findAll();
    }

    @Test
    void findById_debeRetornarVentaCuandoExiste() {
        when(ventaRepository.findById(7L)).thenReturn(Optional.of(venta));

        Venta resultado = ventaService.findById(7L);

        assertNotNull(resultado);
        assertSame(venta, resultado);
        verify(ventaRepository).findById(7L);
    }

    @Test
    void findById_debeRetornarNullCuandoNoExiste() {
        when(ventaRepository.findById(100L)).thenReturn(Optional.empty());

        Venta resultado = ventaService.findById(100L);

        assertNull(resultado);
        verify(ventaRepository).findById(100L);
    }

    @Test
    void save_debeDelegarEnRepositorio() {
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.save(venta);

        assertSame(venta, resultado);
        verify(ventaRepository).save(venta);
    }

    @Test
    void findByUsuarioId_debeRetornarVentasDelUsuario() {
        when(ventaRepository.findByUsuarioId(3L)).thenReturn(List.of(venta));

        List<Venta> resultado = ventaService.findByUsuarioId(3L);

        assertEquals(1, resultado.size());
        assertSame(venta, resultado.get(0));
        verify(ventaRepository).findByUsuarioId(3L);
    }
}
