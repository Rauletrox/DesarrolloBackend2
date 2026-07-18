package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.service.CarritoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
class CarritoControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private CarritoService carritoService;

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeListarCarrito() throws Exception {
        Carrito carrito = new Carrito();
        carrito.setId(1L);
        carrito.setCantidad(2);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("cliente");
        carrito.setUsuario(usuario);
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Leche");
        producto.setPrecio(1290.0);
        producto.setStock(10);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");
        producto.setCategoria(categoria);
        carrito.setProducto(producto);
        when(carritoService.findAll()).thenReturn(List.of(carrito));

        mockMvc.perform(get("/api/carrito"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Leche")));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeEliminarCarrito() throws Exception {
        Carrito carrito = new Carrito();
        carrito.setId(1L);
        when(carritoService.findById(1L)).thenReturn(carrito);

        mockMvc.perform(delete("/api/carrito/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeRetornar404SiCarritoNoExiste() throws Exception {
        when(carritoService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/carrito/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeRetornar404AlActualizarCarritoInexistente() throws Exception {
        when(carritoService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/carrito/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":3,\"usuario\":{\"id\":1},\"producto\":{\"id\":1}}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeRetornar404AlEliminarCarritoInexistente() throws Exception {
        when(carritoService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/carrito/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void debeAgregarCarrito() throws Exception {
        Carrito carrito = new Carrito();
        carrito.setId(1L);
        carrito.setCantidad(3);
        when(carritoService.save(any(Carrito.class))).thenReturn(carrito);

        mockMvc.perform(post("/api/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":3,\"usuario\":{\"id\":1},\"producto\":{\"id\":1}}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"cantidad\":3")));
    }
}
