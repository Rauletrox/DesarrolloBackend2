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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetalleVentaServiceImplTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @InjectMocks
    private DetalleVentaServiceImpl detalleVentaService;

    @Test
    void saveGuardaDetalleVenta() {
        DetalleVenta detalleVenta = new DetalleVenta();
        when(detalleVentaRepository.save(any(DetalleVenta.class))).thenReturn(detalleVenta);

        DetalleVenta result = detalleVentaService.save(detalleVenta);

        assertNotNull(result);
        verify(detalleVentaRepository).save(detalleVenta);
    }

    @Test
    void findByIdRetornaDetalleCuandoExiste() {
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(1L);
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(detalleVenta));

        DetalleVenta result = detalleVentaService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findByIdRetornaNullCuandoNoExiste() {
        when(detalleVentaRepository.findById(9L)).thenReturn(Optional.empty());

        DetalleVenta result = detalleVentaService.findById(9L);

        assertNull(result);
    }

    @Test
    void findByVentaIdDelegatesToRepository() {
        when(detalleVentaRepository.findByVentaId(12L)).thenReturn(List.of());

        List<DetalleVenta> result = detalleVentaService.findByVentaId(12L);

        assertNotNull(result);
        verify(detalleVentaRepository).findByVentaId(12L);
    }
}
