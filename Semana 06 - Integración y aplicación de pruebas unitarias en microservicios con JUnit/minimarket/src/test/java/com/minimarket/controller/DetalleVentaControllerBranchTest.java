package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DetalleVentaController.class)
@Import(SecurityConfig.class)
@WithMockUser
class DetalleVentaControllerBranchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private DetalleVentaService detalleVentaService;

    @Test
    void actualizarDetalleVentaInexistente() throws Exception {
        when(detalleVentaService.findById(77L)).thenReturn(null);

        mockMvc.perform(put("/api/detalle-ventas/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":5,"precio":1500,"venta":{"id":1},"producto":{"id":2}}
                                """))
                .andExpect(status().isNotFound());
    }
}