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

@WebMvcTest(CarritoController.class)
@Import(SecurityConfig.class)
@WithMockUser
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private CarritoService carritoService;

    @Test
    void listarCarritoRetornaContenido() throws Exception {
        when(carritoService.findAll()).thenReturn(List.of(carrito(1L, 2)));

        mockMvc.perform(get("/api/carrito"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cantidad").value(2));
    }

    @Test
    void obtenerCarritoPorIdExistente() throws Exception {
        when(carritoService.findById(1L)).thenReturn(carrito(1L, 2));

        mockMvc.perform(get("/api/carrito/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(2));
    }

    @Test
    void obtenerCarritoPorIdInexistente() throws Exception {
        when(carritoService.findById(9L)).thenReturn(null);

        mockMvc.perform(get("/api/carrito/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    void agregarProductoAlCarritoRetornaOk() throws Exception {
        when(carritoService.save(any(Carrito.class))).thenReturn(carrito(4L, 3));

        mockMvc.perform(post("/api/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":3,"usuario":{"id":1},"producto":{"id":2}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(3));
    }

    @Test
    void actualizarCarritoExistente() throws Exception {
        when(carritoService.findById(1L)).thenReturn(carrito(1L, 2));
        when(carritoService.save(any(Carrito.class))).thenReturn(carrito(1L, 5));

        mockMvc.perform(put("/api/carrito/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":5,"usuario":{"id":1},"producto":{"id":2}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(5));

        verify(carritoService).save(any(Carrito.class));
    }

    @Test
    void eliminarProductoDelCarritoExistente() throws Exception {
        when(carritoService.findById(1L)).thenReturn(carrito(1L, 2));

        mockMvc.perform(delete("/api/carrito/1"))
                .andExpect(status().isNoContent());

        verify(carritoService).deleteById(1L);
    }

    private Carrito carrito(Long id, int cantidad) {
        Carrito carrito = new Carrito();
        carrito.setId(id);
        carrito.setCantidad(cantidad);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        carrito.setUsuario(usuario);

        Producto producto = new Producto();
        producto.setId(2L);
        carrito.setProducto(producto);

        return carrito;
    }
}
