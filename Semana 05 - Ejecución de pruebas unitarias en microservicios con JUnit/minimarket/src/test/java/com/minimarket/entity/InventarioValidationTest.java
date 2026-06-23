package com.minimarket.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InventarioValidationTest {

    private final Validator validator;

    InventarioValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void inventarioValido_noDebeTenerViolaciones() {
        Inventario inventario = crearInventarioValido();

        Set<ConstraintViolation<Inventario>> violations = validator.validate(inventario);

        assertTrue(violations.isEmpty());
    }

    @Test
    void inventarioInvalido_debeReportarCamposObligatorios() {
        Inventario inventario = new Inventario();

        Set<ConstraintViolation<Inventario>> violations = validator.validate(inventario);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("producto")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cantidad")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoMovimiento")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fechaMovimiento")));
    }

    @Test
    void inventarioConProductoCorrecto_debeConservarLaRelacion() {
        Inventario inventario = crearInventarioValido();

        assertNotNull(inventario.getProducto());
        assertEquals("Arroz", inventario.getProducto().getNombre());
    }

    private Inventario crearInventarioValido() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Abarrotes");

        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Arroz");
        producto.setPrecio(1000.0);
        producto.setStock(10);
        producto.setCategoria(categoria);

        Inventario inventario = new Inventario();
        inventario.setId(4L);
        inventario.setProducto(producto);
        inventario.setCantidad(5);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());
        return inventario;
    }
}
