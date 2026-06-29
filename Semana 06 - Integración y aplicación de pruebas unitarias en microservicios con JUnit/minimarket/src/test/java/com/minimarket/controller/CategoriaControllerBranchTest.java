package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.CategoriaService;
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

@WebMvcTest(CategoriaController.class)
@Import(SecurityConfig.class)
@WithMockUser
class CategoriaControllerBranchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private CategoriaService categoriaService;

    @Test
    void actualizarCategoriaInexistente() throws Exception {
        when(categoriaService.findById(77L)).thenReturn(null);

        mockMvc.perform(put("/api/categorias/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Snacks"}
                                """))
                .andExpect(status().isNotFound());
    }
}