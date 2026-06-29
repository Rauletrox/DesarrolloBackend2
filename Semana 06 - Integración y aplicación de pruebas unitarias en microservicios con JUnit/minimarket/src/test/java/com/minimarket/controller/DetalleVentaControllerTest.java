package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.DetalleVentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DetalleVentaController.class)
@Import(SecurityConfig.class)
@WithMockUser
class DetalleVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private DetalleVentaService detalleVentaService;

    @Test
    void listarDetalleVentasRetornaContenido() throws Exception {
        when(detalleVentaService.findAll()).thenReturn(List.of(detalleVenta(1L, 2)));

        mockMvc.perform(get("/api/detalle-ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cantidad").value(2));
    }

    @Test
    void obtenerDetalleVentaPorIdExistente() throws Exception {
        when(detalleVentaService.findById(1L)).thenReturn(detalleVenta(1L, 2));

        mockMvc.perform(get("/api/detalle-ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(2));
    }

    @Test
    void obtenerDetalleVentaPorIdInexistente() throws Exception {
        when(detalleVentaService.findById(9L)).thenReturn(null);

        mockMvc.perform(get("/api/detalle-ventas/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardarDetalleVentaRetornaDetalleGuardado() throws Exception {
        when(detalleVentaService.save(any(DetalleVenta.class))).thenReturn(detalleVenta(3L, 4));

        mockMvc.perform(post("/api/detalle-ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":4,"precio":1200,"venta":{"id":1},"producto":{"id":2}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(4));
    }

    @Test
    void actualizarDetalleVentaExistente() throws Exception {
        when(detalleVentaService.findById(1L)).thenReturn(detalleVenta(1L, 2));
        when(detalleVentaService.save(any(DetalleVenta.class))).thenReturn(detalleVenta(1L, 5));

        mockMvc.perform(put("/api/detalle-ventas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":5,"precio":1500,"venta":{"id":1},"producto":{"id":2}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(5));

        verify(detalleVentaService).save(any(DetalleVenta.class));
    }

    @Test
    void eliminarDetalleVentaExistente() throws Exception {
        when(detalleVentaService.findById(1L)).thenReturn(detalleVenta(1L, 2));

        mockMvc.perform(delete("/api/detalle-ventas/1"))
                .andExpect(status().isNoContent());

        verify(detalleVentaService).deleteById(1L);
    }

    private DetalleVenta detalleVenta(Long id, int cantidad) {
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(id);
        detalleVenta.setCantidad(cantidad);
        detalleVenta.setPrecio(1200.0);

        Venta venta = new Venta();
        venta.setId(1L);
        detalleVenta.setVenta(venta);

        Producto producto = new Producto();
        producto.setId(2L);
        detalleVenta.setProducto(producto);

        return detalleVenta;
    }
}
