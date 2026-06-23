package com.minimarket.controller;

import com.minimarket.entity.Venta;
import com.minimarket.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

    private Venta venta;

    @BeforeEach
    void setUp() {
        venta = new Venta();
        venta.setId(7L);
        venta.setFecha(new Date());
    }

    @Test
    void listarVentas_debeRetornarLista() {
        when(ventaService.findAll()).thenReturn(List.of(venta));

        List<Venta> resultado = ventaController.listarVentas();

        assertEquals(1, resultado.size());
        assertSame(venta, resultado.get(0));
        verify(ventaService).findAll();
    }

    @Test
    void obtenerVentaPorId_debeRetornarOkCuandoExiste() {
        when(ventaService.findById(7L)).thenReturn(venta);

        ResponseEntity<Venta> response = ventaController.obtenerVentaPorId(7L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(venta, response.getBody());
        verify(ventaService).findById(7L);
    }

    @Test
    void obtenerVentaPorId_debeRetornar404CuandoNoExiste() {
        when(ventaService.findById(7L)).thenReturn(null);

        ResponseEntity<Venta> response = ventaController.obtenerVentaPorId(7L);

        assertEquals(404, response.getStatusCode().value());
        verify(ventaService).findById(7L);
    }

    @Test
    void guardarVenta_debeDelegarEnServicio() {
        when(ventaService.save(venta)).thenReturn(venta);

        Venta resultado = ventaController.guardarVenta(venta);

        assertSame(venta, resultado);
        verify(ventaService).save(venta);
    }
}
