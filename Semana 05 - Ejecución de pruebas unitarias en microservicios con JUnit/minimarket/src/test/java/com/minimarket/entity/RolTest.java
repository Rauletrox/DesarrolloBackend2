package com.minimarket.entity;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    @Test
    void rol_debePermitirAsignarUsuarios() {
        Rol rol = new Rol("ADMIN");
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setUsername("cliente1");
        usuario.setPassword("secret");

        rol.setUsuarios(Set.of(usuario));

        assertEquals(1L, rol.getId());
        assertEquals("ADMIN", rol.getNombre());
        assertEquals(1, rol.getUsuarios().size());
        assertSame(usuario, rol.getUsuarios().iterator().next());
    }
}
