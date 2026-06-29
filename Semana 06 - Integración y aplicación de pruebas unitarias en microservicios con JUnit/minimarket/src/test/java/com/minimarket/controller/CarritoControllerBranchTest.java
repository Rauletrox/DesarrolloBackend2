package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.CarritoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarritoController.class)
@Import(SecurityConfig.class)
@WithMockUser
class CarritoControllerBranchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private CarritoService carritoService;

    @Test
    void actualizarCarritoInexistente() throws Exception {
        when(carritoService.findById(77L)).thenReturn(null);

        mockMvc.perform(put("/api/carrito/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":5,"usuario":{"id":1},"producto":{"id":2}}
                                """))
                .andExpect(status().isNotFound());
    }
}