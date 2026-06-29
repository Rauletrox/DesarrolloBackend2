package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class ProductoControllerBranchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private ProductoService productoService;

    @Test
    void obtenerProductoPorIdExistente() throws Exception {
        when(productoService.findById(1L)).thenReturn(producto(1L, "Leche"));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Leche"));
    }

    @Test
    void obtenerProductoPorIdInexistente() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarProductoExistente() throws Exception {
        when(productoService.findById(1L)).thenReturn(producto(1L, "Leche"));
        when(productoService.save(any(Producto.class))).thenReturn(producto(1L, "Leche Descremada"));

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Leche Descremada","precio":1800,"stock":12,"categoria":{"id":1}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Leche Descremada"));

        verify(productoService).save(any(Producto.class));
    }

    @Test
    void actualizarProductoInexistente() throws Exception {
        when(productoService.findById(77L)).thenReturn(null);

        mockMvc.perform(put("/api/productos/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Leche Descremada","precio":1800,"stock":12,"categoria":{"id":1}}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarProductoExistente() throws Exception {
        when(productoService.findById(1L)).thenReturn(producto(1L, "Leche"));

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService).deleteById(1L);
    }

    @Test
    void eliminarProductoInexistente() throws Exception {
        when(productoService.findById(404L)).thenReturn(null);

        mockMvc.perform(delete("/api/productos/404"))
                .andExpect(status().isNotFound());
    }

    private Producto producto(Long id, String nombre) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(1200.0);
        producto.setStock(10);
        producto.setCategoria(new Categoria());
        return producto;
    }
}