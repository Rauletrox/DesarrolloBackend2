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

@WebMvcTest(CategoriaController.class)
@Import(SecurityConfig.class)
@WithMockUser
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private CategoriaService categoriaService;

    @Test
    void listarCategoriasRetornaContenido() throws Exception {
        when(categoriaService.findAll()).thenReturn(List.of(categoria(1L, "Bebidas")));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Bebidas"));
    }

    @Test
    void obtenerCategoriaPorIdExistente() throws Exception {
        when(categoriaService.findById(1L)).thenReturn(categoria(1L, "Bebidas"));

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bebidas"));
    }

    @Test
    void obtenerCategoriaPorIdInexistente() throws Exception {
        when(categoriaService.findById(9L)).thenReturn(null);

        mockMvc.perform(get("/api/categorias/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardarCategoriaRetornaCategoriaGuardada() throws Exception {
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria(3L, "Abarrotes"));

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Abarrotes"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Abarrotes"));
    }

    @Test
    void actualizarCategoriaExistente() throws Exception {
        when(categoriaService.findById(1L)).thenReturn(categoria(1L, "Bebidas"));
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria(1L, "Snacks"));

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Snacks"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Snacks"));

        verify(categoriaService).save(any(Categoria.class));
    }

    @Test
    void eliminarCategoriaExistente() throws Exception {
        when(categoriaService.findById(1L)).thenReturn(categoria(1L, "Bebidas"));

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());

        verify(categoriaService).deleteById(1L);
    }

    private Categoria categoria(Long id, String nombre) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombre(nombre);
        return categoria;
    }
}
