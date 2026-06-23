package com.minimarket.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DetalleVentaTest {

    @Test
    void detalleVenta_debePermitirAsignarCampos() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Abarrotes");

        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Arroz");
        producto.setPrecio(1000.0);
        producto.setStock(10);
        producto.setCategoria(categoria);

        Venta venta = new Venta();
        venta.setId(3L);

        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(4L);
        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(2);
        detalleVenta.setPrecio(2000.0);

        assertEquals(4L, detalleVenta.getId());
        assertSame(venta, detalleVenta.getVenta());
        assertSame(producto, detalleVenta.getProducto());
        assertEquals(2, detalleVenta.getCantidad());
        assertEquals(2000.0, detalleVenta.getPrecio());
    }
}
