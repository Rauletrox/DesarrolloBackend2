package com.minimarket.controller;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void listarUsuariosRetornaContenido() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuario(1L, "admin")));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @Test
    void obtenerUsuarioPorIdExistente() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario(1L, "admin")));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    void obtenerUsuarioPorIdInexistente() throws Exception {
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardarUsuarioRetornaUsuarioGuardado() throws Exception {
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario(3L, "nuevo"));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"nuevo","password":"123","roles":[{"nombre":"USER"}]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("nuevo"));
    }

    @Test
    void actualizarUsuarioExistente() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario(1L, "admin")));
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario(1L, "admin-edited"));

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"admin-edited","password":"123","roles":[{"nombre":"ADMIN"}]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin-edited"));

        verify(usuarioService).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuarioInexistente() throws Exception {
        when(usuarioService.findById(77L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuarios/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"no-existe","password":"123","roles":[{"nombre":"ADMIN"}]}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarUsuarioExistente() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario(1L, "admin")));

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService).deleteById(1L);
    }

    @Test
    void eliminarUsuarioInexistente() throws Exception {
        when(usuarioService.findById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/usuarios/404"))
                .andExpect(status().isNotFound());
    }

    private Usuario usuario(Long id, String username) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setUsername(username);
        usuario.setPassword("encoded");
        usuario.setRoles(Set.of(new Rol("ADMIN")));
        return usuario;
    }
}
