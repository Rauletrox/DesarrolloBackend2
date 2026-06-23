package com.minimarket.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CarritoValidationTest {

    private final Validator validator;

    CarritoValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void carritoValido_noDebeTenerViolaciones() {
        Carrito carrito = crearCarritoValido();

        Set<ConstraintViolation<Carrito>> violations = validator.validate(carrito);

        assertTrue(violations.isEmpty());
    }

    @Test
    void carritoInvalido_debeReportarViolaciones() {
        Carrito carrito = new Carrito();

        Set<ConstraintViolation<Carrito>> violations = validator.validate(carrito);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("usuario")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("producto")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cantidad")));
    }

    @Test
    void carritoConStockSuficiente_debeCumplirLaEscenaDeNegocio() {
        Carrito carrito = crearCarritoValido();

        assertTrue(carrito.getProducto().getStock() >= carrito.getCantidad());
    }

    private Carrito crearCarritoValido() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Abarrotes");

        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Arroz");
        producto.setPrecio(1000.0);
        producto.setStock(10);
        producto.setCategoria(categoria);

        Usuario usuario = new Usuario();
        usuario.setId(3L);
        usuario.setUsername("cliente1");
        usuario.setPassword("secret");

        Carrito carrito = new Carrito();
        carrito.setId(4L);
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
        carrito.setCantidad(3);
        return carrito;
    }
}
