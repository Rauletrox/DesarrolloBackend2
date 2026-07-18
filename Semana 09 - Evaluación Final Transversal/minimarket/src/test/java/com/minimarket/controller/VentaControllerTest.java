package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class VentaControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private VentaService ventaService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeListarVentas() throws Exception {
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setFecha(new Date());
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");
        venta.setUsuario(usuario);
        when(ventaService.findAll()).thenReturn(List.of(venta));

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("admin")));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeGuardarVenta() throws Exception {
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setFecha(new Date());
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("cliente");
        venta.setUsuario(usuario);
        when(ventaService.save(any(Venta.class))).thenReturn(venta);

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":{\"id\":1},\"fecha\":\"2026-07-17T00:00:00.000+00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("cliente")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeRetornar404SiVentaNoExiste() throws Exception {
        when(ventaService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/ventas/99"))
                .andExpect(status().isNotFound());
    }
}
