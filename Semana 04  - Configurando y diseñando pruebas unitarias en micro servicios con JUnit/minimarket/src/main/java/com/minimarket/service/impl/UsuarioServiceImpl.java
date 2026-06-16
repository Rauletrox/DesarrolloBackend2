package com.minimarket.service.impl;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Set<String> ROLES_VALIDOS_PARA_VENTA = Set.of("ADMIN", "CAJERO", "VENDEDOR");

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (!datosObligatoriosCompletos(usuario)) { // Se valida que el usuario tenga todos los campos obligatorios antes de persistirlo.
            throw new IllegalArgumentException("El usuario debe tener nombre, apellido, email, direccion, username, password y roles."); // Se informa el motivo exacto del rechazo.
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean datosObligatoriosCompletos(Long usuarioId) {
        return usuarioRepository.findById(usuarioId) // Se consulta al repositorio para simular la verificación contra base de datos.
                .map(this::datosObligatoriosCompletos) // Si el usuario existe, se evalúa su información obligatoria.
                .orElse(false); // Si el usuario no existe, se considera inválido.
    }

    @Override
    public boolean tieneRolValidoParaVenta(Long usuarioId) {
        return usuarioRepository.findById(usuarioId) // Se busca el usuario en el repositorio para evaluar sus roles reales.
                .map(this::tieneRolValidoParaVenta) // Si el usuario existe, se revisa si su rol permite vender.
                .orElse(false); // Si el usuario no existe, no puede ejecutar ventas.
    }

    private boolean datosObligatoriosCompletos(Usuario usuario) {
        return usuario != null // Se asegura que el objeto usuario exista.
                && textoPresente(usuario.getNombre()) // Se valida que el nombre no venga vacío.
                && textoPresente(usuario.getApellido()) // Se valida que el apellido no venga vacío.
                && textoPresente(usuario.getEmail()) // Se valida que el correo no venga vacío.
                && textoPresente(usuario.getDireccion()) // Se valida que la dirección no venga vacía.
                && textoPresente(usuario.getUsername()) // Se valida que el nombre de usuario no venga vacío.
                && textoPresente(usuario.getPassword()) // Se valida que la contraseña no venga vacía.
                && usuario.getRoles() != null // Se asegura que la colección de roles exista.
                && !usuario.getRoles().isEmpty(); // Se asegura que exista al menos un rol asociado.
    }

    private boolean tieneRolValidoParaVenta(Usuario usuario) {
        return usuario != null // Se asegura que el usuario exista antes de revisar su lista de roles.
                && usuario.getRoles() != null // Se valida que la colección de roles no sea nula.
                && usuario.getRoles().stream() // Se recorre cada rol del usuario.
                .filter(rol -> rol != null) // Se descartan referencias nulas para evitar errores.
                .map(Rol::getNombre) // Se toma el nombre de cada rol para compararlo.
                .filter(this::textoPresente) // Se descartan nombres vacíos o en blanco.
                .map(nombre -> nombre.trim().toUpperCase(Locale.ROOT)) // Se normaliza el texto para comparar sin importar mayúsculas o espacios.
                .anyMatch(ROLES_VALIDOS_PARA_VENTA::contains); // Se confirma que al menos un rol pertenezca al conjunto permitido.
    }

    private boolean textoPresente(String valor) {
        return valor != null // Se valida que la referencia no sea nula.
                && !valor.trim().isEmpty(); // Se valida que el contenido no esté vacío ni compuesto solo por espacios.
    }
}
