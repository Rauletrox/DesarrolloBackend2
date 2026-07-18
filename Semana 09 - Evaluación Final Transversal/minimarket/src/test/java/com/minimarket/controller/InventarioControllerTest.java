package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class InventarioControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private InventarioService inventarioService;

    @Test @WithMockUser(roles = "CLIENTE")
    void debeListarMovimientos() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Leche");
        producto.setPrecio(1290.0);
        producto.setStock(10);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");
        producto.setCategoria(categoria);

        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProducto(producto);
        inventario.setCantidad(5);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());
        when(inventarioService.findAll()).thenReturn(java.util.List.of(inventario));

        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Entrada")));
    }

    @Test @WithMockUser(roles = "ADMIN")
    void debeRegistrarMovimiento() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProducto(producto);
        inventario.setCantidad(5);
        inventario.setTipoMovimiento("Salida");
        inventario.setFechaMovimiento(new Date());
        when(inventarioService.save(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(post("/api/inventario")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"producto\":{\"id\":1},\"cantidad\":5,\"tipoMovimiento\":\"Salida\",\"fechaMovimiento\":\"2026-07-16T00:00:00.000+00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Salida")));
    }

    @Test @WithMockUser(roles = "CLIENTE")
    void debeRetornar404SiMovimientoNoExiste() throws Exception {
        when(inventarioService.findById(99L)).thenReturn(null);
        mockMvc.perform(get("/api/inventario/99"))
                .andExpect(status().isNotFound());
    }

    @Test @WithMockUser(roles = "ADMIN")
    void debeRetornar404AlActualizarMovimientoInexistente() throws Exception {
        when(inventarioService.findById(99L)).thenReturn(null);
        mockMvc.perform(put("/api/inventario/99")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"producto\":{\"id\":1},\"cantidad\":5,\"tipoMovimiento\":\"Salida\",\"fechaMovimiento\":\"2026-07-16T00:00:00.000+00:00\"}"))
                .andExpect(status().isNotFound());
    }

    @Test @WithMockUser(roles = "ADMIN")
    void debeRetornar404AlEliminarMovimientoInexistente() throws Exception {
        when(inventarioService.findById(99L)).thenReturn(null);
        mockMvc.perform(delete("/api/inventario/99"))
                .andExpect(status().isNotFound());
    }

    @Test @WithMockUser(roles = "CLIENTE")
    void debeRetornar404SiMovimientoNoExisteEnDetalle() throws Exception {
        when(inventarioService.findById(99L)).thenReturn(null);
        mockMvc.perform(get("/api/inventario/99"))
                .andExpect(status().isNotFound());
    }
}
