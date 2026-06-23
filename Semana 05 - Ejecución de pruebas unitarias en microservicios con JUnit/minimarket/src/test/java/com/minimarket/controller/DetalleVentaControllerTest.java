package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.service.DetalleVentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleVentaControllerTest {

    @Mock
    private DetalleVentaService detalleVentaService;

    @InjectMocks
    private DetalleVentaController detalleVentaController;

    private DetalleVenta detalleVenta;

    @BeforeEach
    void setUp() {
        detalleVenta = new DetalleVenta();
        detalleVenta.setId(11L);
        detalleVenta.setCantidad(2);
        detalleVenta.setPrecio(2500.0);
    }

    @Test
    void listarDetalleVentas_debeRetornarLista() {
        when(detalleVentaService.findAll()).thenReturn(List.of(detalleVenta));

        List<DetalleVenta> resultado = detalleVentaController.listarDetalleVentas();

        assertEquals(1, resultado.size());
        assertSame(detalleVenta, resultado.get(0));
        verify(detalleVentaService).findAll();
    }

    @Test
    void obtenerDetalleVentaPorId_debeRetornarOkCuandoExiste() {
        when(detalleVentaService.findById(11L)).thenReturn(detalleVenta);

        ResponseEntity<DetalleVenta> response = detalleVentaController.obtenerDetalleVentaPorId(11L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(detalleVenta, response.getBody());
        verify(detalleVentaService).findById(11L);
    }

    @Test
    void obtenerDetalleVentaPorId_debeRetornar404CuandoNoExiste() {
        when(detalleVentaService.findById(11L)).thenReturn(null);

        ResponseEntity<DetalleVenta> response = detalleVentaController.obtenerDetalleVentaPorId(11L);

        assertEquals(404, response.getStatusCode().value());
        verify(detalleVentaService).findById(11L);
    }

    @Test
    void guardarDetalleVenta_debeDelegarEnServicio() {
        when(detalleVentaService.save(detalleVenta)).thenReturn(detalleVenta);

        DetalleVenta resultado = detalleVentaController.guardarDetalleVenta(detalleVenta);

        assertSame(detalleVenta, resultado);
        verify(detalleVentaService).save(detalleVenta);
    }

    @Test
    void actualizarDetalleVenta_debeRetornarOkCuandoExiste() {
        when(detalleVentaService.findById(11L)).thenReturn(detalleVenta);
        when(detalleVentaService.save(any(DetalleVenta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<DetalleVenta> response = detalleVentaController.actualizarDetalleVenta(11L, detalleVenta);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(11L, response.getBody().getId());
        verify(detalleVentaService).save(detalleVenta);
    }

    @Test
    void actualizarDetalleVenta_debeRetornar404CuandoNoExiste() {
        when(detalleVentaService.findById(11L)).thenReturn(null);

        ResponseEntity<DetalleVenta> response = detalleVentaController.actualizarDetalleVenta(11L, detalleVenta);

        assertEquals(404, response.getStatusCode().value());
        verify(detalleVentaService, never()).save(any());
    }

    @Test
    void eliminarDetalleVenta_debeRetornarNoContentCuandoExiste() {
        when(detalleVentaService.findById(11L)).thenReturn(detalleVenta);

        ResponseEntity<Void> response = detalleVentaController.eliminarDetalleVenta(11L);

        assertEquals(204, response.getStatusCode().value());
        verify(detalleVentaService).deleteById(11L);
    }

    @Test
    void eliminarDetalleVenta_debeRetornar404CuandoNoExiste() {
        when(detalleVentaService.findById(11L)).thenReturn(null);

        ResponseEntity<Void> response = detalleVentaController.eliminarDetalleVenta(11L);

        assertEquals(404, response.getStatusCode().value());
        verify(detalleVentaService, never()).deleteById(anyLong());
    }
}
