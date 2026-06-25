package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.InventarioService;
import com.minimarket.service.ProductoService;
import com.minimarket.service.VentaService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ProductoController.class, InventarioController.class, VentaController.class})
@Import(SecurityConfig.class)
class SecurityAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private InventarioService inventarioService;

    @MockBean
    private VentaService ventaService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPuedeCrearProducto() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Arroz");
        producto.setPrecio(1200.0);
        producto.setStock(15);
        producto.setCategoria(new Categoria());

        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Arroz","precio":1200,"stock":15,"categoria":{"id":1}}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void clienteNoPuedeCrearProducto() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Arroz","precio":1200,"stock":15,"categoria":{"id":1}}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPuedeRegistrarInventario() throws Exception {
        when(inventarioService.save(any())).thenReturn(null);

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":5,"tipoMovimiento":"Entrada"}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CAJERO")
    void cajeroPuedeCrearVenta() throws Exception {
        Venta venta = new Venta();
        when(ventaService.save(any(Venta.class))).thenReturn(venta);

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario":{"id":1},"fecha":"2026-06-24T00:00:00.000+0000"}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void clienteNoPuedeCrearVenta() throws Exception {
        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario":{"id":1},"fecha":"2026-06-24T00:00:00.000+0000"}
                                """))
                .andExpect(status().isForbidden());
    }
}
