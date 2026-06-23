package com.minimarket.service.impl;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.repository.DetalleVentaRepository;
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
class DetalleVentaServiceImplTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @InjectMocks
    private DetalleVentaServiceImpl detalleVentaService;

    private DetalleVenta detalleVenta;

    @BeforeEach
    void setUp() {
        detalleVenta = new DetalleVenta();
        detalleVenta.setId(11L);
        detalleVenta.setCantidad(2);
        detalleVenta.setPrecio(2500.0);
    }

    @Test
    void findAll_debeRetornarDetalleVentas() {
        when(detalleVentaRepository.findAll()).thenReturn(List.of(detalleVenta));

        List<DetalleVenta> resultado = detalleVentaService.findAll();

        assertEquals(1, resultado.size());
        assertSame(detalleVenta, resultado.get(0));
        verify(detalleVentaRepository).findAll();
    }

    @Test
    void findById_debeRetornarDetalleCuandoExiste() {
        when(detalleVentaRepository.findById(11L)).thenReturn(Optional.of(detalleVenta));

        DetalleVenta resultado = detalleVentaService.findById(11L);

        assertNotNull(resultado);
        assertSame(detalleVenta, resultado);
        verify(detalleVentaRepository).findById(11L);
    }

    @Test
    void findById_debeRetornarNullCuandoNoExiste() {
        when(detalleVentaRepository.findById(22L)).thenReturn(Optional.empty());

        DetalleVenta resultado = detalleVentaService.findById(22L);

        assertNull(resultado);
        verify(detalleVentaRepository).findById(22L);
    }

    @Test
    void save_debeDelegarEnRepositorio() {
        when(detalleVentaRepository.save(detalleVenta)).thenReturn(detalleVenta);

        DetalleVenta resultado = detalleVentaService.save(detalleVenta);

        assertSame(detalleVenta, resultado);
        verify(detalleVentaRepository).save(detalleVenta);
    }

    @Test
    void deleteById_debeDelegarEnRepositorio() {
        detalleVentaService.deleteById(11L);

        verify(detalleVentaRepository).deleteById(11L);
    }

    @Test
    void findByVentaId_debeRetornarDetallePorVenta() {
        when(detalleVentaRepository.findByVentaId(7L)).thenReturn(List.of(detalleVenta));

        List<DetalleVenta> resultado = detalleVentaService.findByVentaId(7L);

        assertEquals(1, resultado.size());
        assertSame(detalleVenta, resultado.get(0));
        verify(detalleVentaRepository).findByVentaId(7L);
    }
}
