package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.service.DetalleVentaService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DetalleVentaControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private DetalleVentaService detalleVentaService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeListarDetalleVentas() throws Exception {
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(1L);
        detalleVenta.setCantidad(2);
        detalleVenta.setPrecio(1290.0);
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setFecha(new Date());
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");
        venta.setUsuario(usuario);
        detalleVenta.setVenta(venta);
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Leche");
        producto.setPrecio(1290.0);
        producto.setStock(10);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");
        producto.setCategoria(categoria);
        detalleVenta.setProducto(producto);
        when(detalleVentaService.findAll()).thenReturn(List.of(detalleVenta));

        mockMvc.perform(get("/api/detalle-ventas"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Leche")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeGuardarDetalleVenta() throws Exception {
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(1L);
        detalleVenta.setCantidad(1);
        detalleVenta.setPrecio(990.0);
        when(detalleVentaService.save(any(DetalleVenta.class))).thenReturn(detalleVenta);

        mockMvc.perform(post("/api/detalle-ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":1,\"precio\":990.0,\"venta\":{\"id\":1},\"producto\":{\"id\":1}}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"precio\":990.0")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeRetornar404SiDetalleVentaNoExiste() throws Exception {
        when(detalleVentaService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/detalle-ventas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeRetornar404AlActualizarDetalleVentaInexistente() throws Exception {
        when(detalleVentaService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/detalle-ventas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":1,\"precio\":990.0,\"venta\":{\"id\":1},\"producto\":{\"id\":1}}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeRetornar404AlEliminarDetalleVentaInexistente() throws Exception {
        when(detalleVentaService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/detalle-ventas/99"))
                .andExpect(status().isNotFound());
    }
}
