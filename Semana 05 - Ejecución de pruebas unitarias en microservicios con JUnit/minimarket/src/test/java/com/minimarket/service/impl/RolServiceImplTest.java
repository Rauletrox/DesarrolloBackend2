package com.minimarket.service.impl;

import com.minimarket.entity.Rol;
import com.minimarket.repository.RolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolServiceImplTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    @Test
    void findByNombre_debeRetornarRolCuandoExiste() {
        Rol rol = new Rol("ADMIN");
        when(rolRepository.findByNombre("ADMIN")).thenReturn(Optional.of(rol));

        Optional<Rol> resultado = rolService.findByNombre("ADMIN");

        assertTrue(resultado.isPresent());
        assertSame(rol, resultado.get());
        verify(rolRepository).findByNombre("ADMIN");
    }

    @Test
    void findByNombre_debeRetornarOptionalVacioCuandoNoExiste() {
        when(rolRepository.findByNombre("USER")).thenReturn(Optional.empty());

        Optional<Rol> resultado = rolService.findByNombre("USER");

        assertTrue(resultado.isEmpty());
        verify(rolRepository).findByNombre("USER");
    }
}
