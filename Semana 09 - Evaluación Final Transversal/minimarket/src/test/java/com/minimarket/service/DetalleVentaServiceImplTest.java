package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.service.impl.DetalleVentaServiceImpl;
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
class DetalleVentaServiceImplTest {

    @Mock private DetalleVentaRepository detalleVentaRepository;
    @InjectMocks private DetalleVentaServiceImpl detalleVentaService;

    @Test
    void debeListarDetalleVentas() {
        when(detalleVentaRepository.findAll()).thenReturn(List.of(new DetalleVenta()));
        assertEquals(1, detalleVentaService.findAll().size());
    }

    @Test
    void findByIdDebeRetornarDetalleVentaONull() {
        DetalleVenta detalleVenta = new DetalleVenta();
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(detalleVenta));
        assertEquals(detalleVenta, detalleVentaService.findById(1L));
        when(detalleVentaRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(detalleVentaService.findById(2L));
    }

    @Test
    void saveDebeDelegarAlRepositorio() {
        DetalleVenta detalleVenta = new DetalleVenta();
        when(detalleVentaRepository.save(any())).thenReturn(detalleVenta);
        assertEquals(detalleVenta, detalleVentaService.save(detalleVenta));
    }

    @Test
    void deleteByIdDebeDelegarAlRepositorio() {
        detalleVentaService.deleteById(1L);
        verify(detalleVentaRepository).deleteById(1L);
    }

    @Test
    void findByVentaIdDebeDelegarAlRepositorio() {
        when(detalleVentaRepository.findByVentaId(1L)).thenReturn(List.of(new DetalleVenta()));
        assertEquals(1, detalleVentaService.findByVentaId(1L).size());
    }
}
