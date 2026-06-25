package com.minimarket.service;

import com.minimarket.entity.Inventario;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    @Test
    void saveRegistraMovimiento() {
        Inventario inventario = new Inventario();
        inventario.setCantidad(5);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());

        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario result = inventarioService.save(inventario);

        assertNotNull(result);
        assertEquals("Entrada", result.getTipoMovimiento());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void findByIdRetornaMovimiento() {
        Inventario inventario = new Inventario();
        inventario.setId(7L);
        when(inventarioRepository.findById(7L)).thenReturn(Optional.of(inventario));

        Inventario result = inventarioService.findById(7L);

        assertNotNull(result);
        assertEquals(7L, result.getId());
    }

    @Test
    void findByProductoIdUsaRepositorio() {
        when(inventarioRepository.findByProductoId(12L)).thenReturn(List.of());

        List<Inventario> result = inventarioService.findByProductoId(12L);

        assertNotNull(result);
        verify(inventarioRepository).findByProductoId(12L);
    }
}
