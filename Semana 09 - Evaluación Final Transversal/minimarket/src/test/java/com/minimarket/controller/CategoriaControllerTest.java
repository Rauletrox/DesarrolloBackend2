package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
class CategoriaControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private CategoriaService categoriaService;

    @Test @WithMockUser(roles = "CLIENTE")
    void debeListarCategorias() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");
        when(categoriaService.findAll()).thenReturn(java.util.List.of(categoria));
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Bebidas")));
    }

    @Test @WithMockUser(roles = "ADMIN")
    void debeCrearCategoria() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Abarrotes");
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);
        mockMvc.perform(post("/api/categorias")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Abarrotes\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Abarrotes")));
    }

    @Test @WithMockUser(roles = "CLIENTE")
    void debeRetornar404SiCategoriaNoExiste() throws Exception {
        when(categoriaService.findById(99L)).thenReturn(null);
        mockMvc.perform(get("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }

    @Test @WithMockUser(roles = "ADMIN")
    void debeRetornar404AlActualizarCategoriaInexistente() throws Exception {
        when(categoriaService.findById(99L)).thenReturn(null);
        mockMvc.perform(put("/api/categorias/99")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Abarrotes\"}"))
                .andExpect(status().isNotFound());
    }

    @Test @WithMockUser(roles = "ADMIN")
    void debeRetornar404AlEliminarCategoriaInexistente() throws Exception {
        when(categoriaService.findById(99L)).thenReturn(null);
        mockMvc.perform(delete("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeRetornar404SiCategoriaNoExisteEnActualizacionNoAplica() throws Exception {
        when(categoriaService.findById(99L)).thenReturn(null);
        mockMvc.perform(put("/api/categorias/99")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Abarrotes\"}"))
                .andExpect(status().isNotFound());
    }
}
