package com.minimarket.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {

    @Test
    void categoria_debePermitirAsignarCampos() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Limpieza");

        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Jabon");
        producto.setPrecio(500.0);
        producto.setStock(10);
        producto.setCategoria(categoria);

        categoria.setProductos(List.of(producto));

        assertEquals(1L, categoria.getId());
        assertEquals("Limpieza", categoria.getNombre());
        assertEquals(1, categoria.getProductos().size());
        assertSame(producto, categoria.getProductos().get(0));
    }
}
