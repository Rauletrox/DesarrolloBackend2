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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @Test
    void saveGuardaVenta() {
        Venta venta = new Venta();
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Venta result = ventaService.save(venta);

        assertNotNull(result);
        verify(ventaRepository).save(venta);
    }

    @Test
    void findByUsuarioIdObtieneVentas() {
        when(ventaRepository.findByUsuarioId(3L)).thenReturn(List.of());

        List<Venta> result = ventaService.findByUsuarioId(3L);

        assertNotNull(result);
        verify(ventaRepository).findByUsuarioId(3L);
    }

    @Test
    void findByIdRetornaVenta() {
        Venta venta = new Venta();
        venta.setId(99L);
        when(ventaRepository.findById(99L)).thenReturn(Optional.of(venta));

        Venta result = ventaService.findById(99L);

        assertNotNull(result);
        assertEquals(99L, result.getId());
    }

    @Test
    void findByIdRetornaNullCuandoNoExiste() {
        when(ventaRepository.findById(11L)).thenReturn(Optional.empty());

        Venta result = ventaService.findById(11L);

        assertNull(result);
    }
}
