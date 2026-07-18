package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeListarProductos() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Leche");
        producto.setPrecio(1290.0);
        producto.setStock(10);
        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombre("Lacteos");
        producto.setCategoria(categoria);
        when(productoService.findAll()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Leche")));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeRetornarProductoPorId() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Arroz");
        producto.setPrecio(1390.0);
        producto.setStock(5);
        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombre("Abarrotes");
        producto.setCategoria(categoria);
        when(productoService.findById(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Arroz")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeCrearProducto() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Jugo");
        producto.setPrecio(990.0);
        producto.setStock(8);
        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombre("Bebidas");
        producto.setCategoria(categoria);
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Jugo\",\"precio\":990.0,\"stock\":8,\"categoria\":{\"id\":2,\"nombre\":\"Bebidas\"}}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Jugo")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeActualizarProducto() throws Exception {
        Producto existente = new Producto();
        existente.setId(1L);
        when(productoService.findById(1L)).thenReturn(existente);

        Producto actualizado = new Producto();
        actualizado.setId(1L);
        actualizado.setNombre("Jugo");
        actualizado.setPrecio(1200.0);
        actualizado.setStock(6);
        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombre("Bebidas");
        actualizado.setCategoria(categoria);
        when(productoService.save(any(Producto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Jugo\",\"precio\":1200.0,\"stock\":6,\"categoria\":{\"id\":2,\"nombre\":\"Bebidas\"}}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Jugo")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeEliminarProducto() throws Exception {
        Producto existente = new Producto();
        existente.setId(1L);
        when(productoService.findById(1L)).thenReturn(existente);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeRetornar404SiProductoNoExiste() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeRetornar404AlActualizarProductoInexistente() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/productos/99")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Jugo\",\"precio\":1200.0,\"stock\":6,\"categoria\":{\"id\":2,\"nombre\":\"Bebidas\"}}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeRetornar404AlEliminarProductoInexistente() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/productos/99"))
                .andExpect(status().isNotFound());
    }
}
