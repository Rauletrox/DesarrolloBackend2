package com.minimarket.service;

import com.minimarket.entity.Inventario;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.service.impl.InventarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock private InventarioRepository inventarioRepository;
    @InjectMocks private InventarioServiceImpl inventarioService;

    @Test
    void debeListarInventario() {
        when(inventarioRepository.findAll()).thenReturn(List.of(new Inventario()));
        assertEquals(1, inventarioService.findAll().size());
    }

    @Test
    void findByIdDebeRetornarInventarioONull() {
        Inventario inventario = new Inventario();
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        assertEquals(inventario, inventarioService.findById(1L));
        when(inventarioRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(inventarioService.findById(2L));
    }

    @Test
    void saveDebeDelegarAlRepositorio() {
        Inventario inventario = new Inventario();
        when(inventarioRepository.save(any())).thenReturn(inventario);
        assertEquals(inventario, inventarioService.save(inventario));
    }

    @Test
    void deleteByIdDebeDelegarAlRepositorio() {
        inventarioService.deleteById(1L);
        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void findByProductoIdDebeDelegarAlRepositorio() {
        when(inventarioRepository.findByProductoId(1L)).thenReturn(List.of(new Inventario()));
        assertEquals(1, inventarioService.findByProductoId(1L).size());
    }
}
