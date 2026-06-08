package com.minimarket.security.config;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository rolRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Se crea roles base solicitados por la actividad si la BD parte vacia.
        Rol cliente = obtenerOCrearRol("CLIENTE");
        Rol empleado = obtenerOCrearRol("EMPLEADO");
        Rol administrador = obtenerOCrearRol("ADMINISTRADOR");

        // Se usuarios de prueba con contrasenas BCrypt para validar los niveles de acceso.
        crearUsuarioSiNoExiste("cliente", "Cliente123", cliente);
        crearUsuarioSiNoExiste("empleado", "Empleado123", empleado);
        crearUsuarioSiNoExiste("administrador", "Administrador123", administrador);
    }

    private Rol obtenerOCrearRol(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    Rol rol = new Rol();
                    rol.setNombre(nombre);
                    return rolRepository.save(rol);
                });
    }

    private void crearUsuarioSiNoExiste(String username, String password, Rol rol) {
        usuarioRepository.findByUsername(username).orElseGet(() -> {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setRoles(Set.of(rol));
            return usuarioRepository.save(usuario);
        });
    }
}
