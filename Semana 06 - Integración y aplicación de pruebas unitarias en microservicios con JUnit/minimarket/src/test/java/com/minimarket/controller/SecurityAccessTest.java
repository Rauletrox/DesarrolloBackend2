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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Arroz"));
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
    void adminPuedeActualizarProducto() throws Exception {
        Producto existente = new Producto();
        existente.setId(1L);
        existente.setNombre("Arroz");
        existente.setPrecio(1000.0);
        existente.setStock(10);
        existente.setCategoria(new Categoria());

        Producto actualizado = new Producto();
        actualizado.setId(1L);
        actualizado.setNombre("Arroz Premium");
        actualizado.setPrecio(1500.0);
        actualizado.setStock(12);
        actualizado.setCategoria(new Categoria());

        when(productoService.findById(1L)).thenReturn(existente);
        when(productoService.save(any(Producto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Arroz Premium","precio":1500,"stock":12,"categoria":{"id":1}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Arroz Premium"));

        verify(productoService).save(any(Producto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminRecibeNotFoundSiProductoNoExiste() throws Exception {
        when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Arroz Premium","precio":1500,"stock":12,"categoria":{"id":1}}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPuedeEliminarProducto() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        when(productoService.findById(1L)).thenReturn(producto);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService).deleteById(1L);
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
    @WithMockUser(roles = "USER")
    void clienteNoPuedeRegistrarInventario() throws Exception {
        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":5,"tipoMovimiento":"Entrada"}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPuedeActualizarInventario() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(new com.minimarket.entity.Inventario());
        when(inventarioService.save(any())).thenReturn(new com.minimarket.entity.Inventario());

        mockMvc.perform(put("/api/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cantidad":8,"tipoMovimiento":"Salida"}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPuedeEliminarInventario() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(new com.minimarket.entity.Inventario());

        mockMvc.perform(delete("/api/inventario/1"))
                .andExpect(status().isNoContent());

        verify(inventarioService).deleteById(1L);
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

    @Test
    @WithMockUser(roles = "CAJERO")
    void cajeroPuedeVerListadoDeVentas() throws Exception {
        when(ventaService.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk());
    }
}
