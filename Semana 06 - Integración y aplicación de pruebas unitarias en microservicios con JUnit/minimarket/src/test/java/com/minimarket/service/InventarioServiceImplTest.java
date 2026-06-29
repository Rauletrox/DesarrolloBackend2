package com.minimarket.service;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.service.impl.InventarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    @Test
    void saveRegistraMovimiento() {
        Inventario inventario = inventario("Entrada", 5);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        Inventario result = inventarioService.save(inventario);

        assertNotNull(result);
        assertEquals("Entrada", result.getTipoMovimiento());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void findByIdRetornaMovimiento() {
        Inventario inventario = inventario("Entrada", 5);
        inventario.setId(7L);
        when(inventarioRepository.findById(7L)).thenReturn(Optional.of(inventario));

        Inventario result = inventarioService.findById(7L);

        assertNotNull(result);
        assertEquals(7L, result.getId());
    }

    @Test
    void findByIdRetornaNullCuandoNoExiste() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        Inventario result = inventarioService.findById(99L);

        assertNull(result);
    }

    @Test
    void findByProductoIdUsaRepositorio() {
        when(inventarioRepository.findByProductoId(12L)).thenReturn(List.of());

        List<Inventario> result = inventarioService.findByProductoId(12L);

        assertNotNull(result);
        verify(inventarioRepository).findByProductoId(12L);
    }

    private Inventario inventario(String tipoMovimiento, Integer cantidad) {
        Inventario inventario = new Inventario();
        inventario.setTipoMovimiento(tipoMovimiento);
        inventario.setCantidad(cantidad);
        inventario.setFechaMovimiento(new Date());
        inventario.setProducto(new Producto());
        return inventario;
    }
}
