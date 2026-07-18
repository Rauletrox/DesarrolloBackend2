package com.minimarket.service;

import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;
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
class VentaServiceImplTest {

    @Mock private VentaRepository ventaRepository;
    @InjectMocks private VentaServiceImpl ventaService;

    @Test
    void debeListarVentas() {
        when(ventaRepository.findAll()).thenReturn(List.of(new Venta()));
        assertEquals(1, ventaService.findAll().size());
    }

    @Test
    void findByIdDebeRetornarVentaONull() {
        Venta venta = new Venta();
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        assertEquals(venta, ventaService.findById(1L));
        when(ventaRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(ventaService.findById(2L));
    }

    @Test
    void saveDebeDelegarAlRepositorio() {
        Venta venta = new Venta();
        when(ventaRepository.save(any())).thenReturn(venta);
        assertEquals(venta, ventaService.save(venta));
    }

    @Test
    void findByUsuarioIdDebeDelegarAlRepositorio() {
        when(ventaRepository.findByUsuarioId(1L)).thenReturn(List.of(new Venta()));
        assertEquals(1, ventaService.findByUsuarioId(1L).size());
    }

    @Test
    void deleteNoAplicaEnVentaService() {
        verify(ventaRepository, org.mockito.Mockito.never()).deleteById(any());
    }
}
