package com.minimarket.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VentaTest {

    @Test
    void venta_debePermitirAsignarRelaciones() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("cliente1");
        usuario.setPassword("secret");

        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombre("Abarrotes");

        Producto producto = new Producto();
        producto.setId(3L);
        producto.setNombre("Arroz");
        producto.setPrecio(1000.0);
        producto.setStock(10);
        producto.setCategoria(categoria);

        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(4L);
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(2);
        detalleVenta.setPrecio(2000.0);

        Venta venta = new Venta();
        venta.setId(5L);
        venta.setUsuario(usuario);
        venta.setFecha(new Date());
        venta.setDetalles(List.of(detalleVenta));

        assertEquals(5L, venta.getId());
        assertSame(usuario, venta.getUsuario());
        assertNotNull(venta.getFecha());
        assertEquals(1, venta.getDetalles().size());
        assertSame(detalleVenta, venta.getDetalles().get(0));
    }
}
