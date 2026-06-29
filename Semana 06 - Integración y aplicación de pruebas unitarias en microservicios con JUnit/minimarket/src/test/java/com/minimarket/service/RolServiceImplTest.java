package com.minimarket.service;

import com.minimarket.entity.Rol;
import com.minimarket.repository.RolRepository;
import com.minimarket.service.impl.RolServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RolServiceImplTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    @Test
    void findByNombreRetornaRol() {
        when(rolRepository.findByNombre("ADMIN")).thenReturn(Optional.of(new Rol("ADMIN")));

        Optional<Rol> rol = rolService.findByNombre("ADMIN");

        assertTrue(rol.isPresent());
    }

    @Test
    void findByNombreRetornaVacioCuandoNoExiste() {
        when(rolRepository.findByNombre("NO_EXISTE")).thenReturn(Optional.empty());

        Optional<Rol> rol = rolService.findByNombre("NO_EXISTE");

        assertTrue(rol.isEmpty());
    }
}
