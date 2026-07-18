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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RolServiceImplTest {

    @Mock private RolRepository rolRepository;
    @InjectMocks private RolServiceImpl rolService;

    @Test
    void findByNombreDebeRetornarRol() {
        Rol rol = new Rol();
        rol.setNombre("ROLE_ADMIN");
        when(rolRepository.findByNombre("ROLE_ADMIN")).thenReturn(Optional.of(rol));

        assertTrue(rolService.findByNombre("ROLE_ADMIN").isPresent());
        assertEquals("ROLE_ADMIN", rolService.findByNombre("ROLE_ADMIN").orElseThrow().getNombre());
    }
}
