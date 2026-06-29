package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventarioController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class InventarioControllerBranchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private InventarioService inventarioService;

    @Test
    void obtenerMovimientoPorIdExistente() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(inventario(1L, "Entrada"));

        mockMvc.perform(get("/api/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoMovimiento").value("Entrada"));
    }

    @Test
    void obtenerMovimientoPorIdInexistente() throws Exception {
        when(inventarioService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/inventario/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarMovimientoExistente() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(inventario(1L, "Entrada"));
        when(inventarioService.save(any(Inventario.class))).thenReturn(inventario(1L, "Salida"));

        mockMvc.perform(put("/api/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":8,"tipoMovimiento":"Salida","fechaMovimiento":"2026-06-24T00:00:00.000+0000","producto":{"id":1}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoMovimiento").value("Salida"));

        verify(inventarioService).save(any(Inventario.class));
    }

    @Test
    void actualizarMovimientoInexistente() throws Exception {
        when(inventarioService.findById(77L)).thenReturn(null);

        mockMvc.perform(put("/api/inventario/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":8,"tipoMovimiento":"Salida","fechaMovimiento":"2026-06-24T00:00:00.000+0000","producto":{"id":1}}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarMovimientoExistente() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(inventario(1L, "Entrada"));

        mockMvc.perform(delete("/api/inventario/1"))
                .andExpect(status().isNoContent());

        verify(inventarioService).deleteById(1L);
    }

    @Test
    void eliminarMovimientoInexistente() throws Exception {
        when(inventarioService.findById(404L)).thenReturn(null);

        mockMvc.perform(delete("/api/inventario/404"))
                .andExpect(status().isNotFound());
    }

    private Inventario inventario(Long id, String tipoMovimiento) {
        Inventario inventario = new Inventario();
        inventario.setId(id);
        inventario.setCantidad(5);
        inventario.setTipoMovimiento(tipoMovimiento);
        inventario.setFechaMovimiento(new Date());
        Producto producto = new Producto();
        producto.setId(1L);
        inventario.setProducto(producto);
        return inventario;
    }
}