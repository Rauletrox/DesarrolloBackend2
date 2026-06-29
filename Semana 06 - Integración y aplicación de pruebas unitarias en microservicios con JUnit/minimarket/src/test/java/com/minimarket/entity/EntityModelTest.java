package com.minimarket.entity;

import com.minimarket.security.model.CustomUserDetails;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityModelTest {

    @Test
    void categoriaGuardaDatosBasicos() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        assertEquals(1L, categoria.getId());
        assertEquals("Bebidas", categoria.getNombre());
    }

    @Test
    void productoGuardaRelacionConCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(10L);

        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Leche");
        producto.setPrecio(1500.0);
        producto.setStock(20);
        producto.setCategoria(categoria);

        assertEquals(2L, producto.getId());
        assertEquals("Leche", producto.getNombre());
        assertEquals(1500.0, producto.getPrecio());
        assertEquals(20, producto.getStock());
        assertSame(categoria, producto.getCategoria());
    }

    @Test
    void inventarioGuardaMovimientoCompleto() {
        Producto producto = new Producto();
        producto.setId(5L);

        Inventario inventario = new Inventario();
        inventario.setId(3L);
        inventario.setProducto(producto);
        inventario.setCantidad(7);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date(0));

        assertEquals(3L, inventario.getId());
        assertSame(producto, inventario.getProducto());
        assertEquals(7, inventario.getCantidad());
        assertEquals("Entrada", inventario.getTipoMovimiento());
        assertEquals(new Date(0), inventario.getFechaMovimiento());
    }

    @Test
    void ventaGuardaUsuarioYDetalles() {
        Usuario usuario = new Usuario();
        usuario.setId(8L);

        Venta venta = new Venta();
        venta.setId(4L);
        venta.setUsuario(usuario);
        venta.setFecha(new Date(0));
        venta.setDetalles(List.of(new DetalleVenta()));

        assertEquals(4L, venta.getId());
        assertSame(usuario, venta.getUsuario());
        assertEquals(new Date(0), venta.getFecha());
        assertEquals(1, venta.getDetalles().size());
    }

    @Test
    void carritoGuardaUsuarioProductoYCantidad() {
        Usuario usuario = new Usuario();
        Producto producto = new Producto();

        Carrito carrito = new Carrito();
        carrito.setId(6L);
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
        carrito.setCantidad(2);

        assertEquals(6L, carrito.getId());
        assertSame(usuario, carrito.getUsuario());
        assertSame(producto, carrito.getProducto());
        assertEquals(2, carrito.getCantidad());
    }

    @Test
    void detalleVentaGuardaDatosBasicos() {
        Venta venta = new Venta();
        Producto producto = new Producto();

        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(7L);
        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(3);
        detalleVenta.setPrecio(1200.0);

        assertEquals(7L, detalleVenta.getId());
        assertSame(venta, detalleVenta.getVenta());
        assertSame(producto, detalleVenta.getProducto());
        assertEquals(3, detalleVenta.getCantidad());
        assertEquals(1200.0, detalleVenta.getPrecio());
    }

    @Test
    void rolConstructorYSetterFuncionan() {
        Rol rol = new Rol("ADMIN");
        rol.setId(11L);
        rol.setNombre("CAJERO");

        assertEquals(11L, rol.getId());
        assertEquals("CAJERO", rol.getNombre());
        assertNull(rol.getUsuarios());
    }

    @Test
    void customUserDetailsExponeAutoridadesYCredenciales() {
        Usuario usuario = new Usuario();
        usuario.setUsername("cajero1");
        usuario.setPassword("encoded");
        usuario.setRoles(Set.of(new Rol("CAJERO")));

        CustomUserDetails userDetails = new CustomUserDetails(usuario);

        assertEquals("cajero1", userDetails.getUsername());
        assertEquals("encoded", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("CAJERO")));
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}
