package com.minimarket.service.impl; 

import com.minimarket.entity.Rol; 
import com.minimarket.entity.Usuario; 
import com.minimarket.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//Se activa la integración entre JUnit 5 y Mockito.
@ExtendWith(MockitoExtension.class) 
class UsuarioServiceImplTest { // Se declara la clase de pruebas del servicio de usuarios.

    @Mock // Se crea un mock del repositorio para simular acceso a datos.
    private UsuarioRepository usuarioRepository; // Se declara el repositorio falso que controlaremos en las pruebas.

    @InjectMocks // Se inyectan los mocks en la clase real que queremos probar.
    private UsuarioServiceImpl usuarioService; // Se crea la instancia del servicio con dependencias simuladas.

    @Test // Se marca el método como una prueba unitaria.
    void datosObligatoriosCompletos_debeRetornarTrueCuandoElUsuarioEstaCompleto() { // Se valida que un usuario con todos los datos pase la verificación.
        Usuario usuario = crearUsuarioCompleto("ADMIN"); // Se construye un usuario con toda la información obligatoria.
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario)); // Se simula que el repositorio encuentra el usuario completo.
        boolean resultado = usuarioService.datosObligatoriosCompletos(1L); // Se ejecuta la validación real del servicio.
        assertTrue(resultado); // Se confirma que la validación devuelve verdadero.
        verify(usuarioRepository).findById(1L); // Se confirma que el servicio consultó el repositorio.
    } // Se cierra la prueba positiva de datos obligatorios.

    @Test // Se marca el método como una prueba unitaria.
    void datosObligatoriosCompletos_debeRetornarFalseCuandoFaltaEmail() { // Se valida que un dato faltante invalide al usuario.
        Usuario usuario = crearUsuarioCompleto("ADMIN"); // Se construye un usuario base con datos válidos.
        usuario.setEmail(null); // Se elimina el correo para simular un registro incompleto.
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario)); // Se simula que el usuario existe pero está incompleto.
        boolean resultado = usuarioService.datosObligatoriosCompletos(2L); // Se ejecuta la validación contra el usuario incompleto.
        assertFalse(resultado); // Se confirma que la validación falla.
        verify(usuarioRepository).findById(2L); // Se confirma que el repositorio fue consultado.
    } // Se cierra la prueba negativa de datos obligatorios.

    @Test // Se marca el método como una prueba unitaria.
    void tieneRolValidoParaVenta_debeRetornarTrueConRolAdmin() { // Se valida que un rol permitido habilite operaciones críticas.
        Usuario usuario = crearUsuarioCompleto("ADMIN"); // Se construye un usuario con el rol autorizado.
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario)); // Se simula que el repositorio retorna el usuario con rol válido.
        boolean resultado = usuarioService.tieneRolValidoParaVenta(3L); // Se ejecuta la validación de rol.
        assertTrue(resultado); // Se confirma que el rol permite la operación.
        verify(usuarioRepository).findById(3L); // Se verifica la consulta al repositorio.
    } // Se cierra la prueba positiva de rol válido.

    @Test // Se marca el método como una prueba unitaria.
    void tieneRolValidoParaVenta_debeRetornarFalseConRolInvitado() { // Se valida que un rol no permitido bloquee la operación.
        Usuario usuario = crearUsuarioCompleto("INVITADO"); // Se construye un usuario con un rol fuera del conjunto permitido.
        when(usuarioRepository.findById(4L)).thenReturn(Optional.of(usuario)); // Se simula que el usuario existe en la base de datos.
        boolean resultado = usuarioService.tieneRolValidoParaVenta(4L); // Se ejecuta la validación de roles.
        assertFalse(resultado); // Se confirma que el rol no autorizado falla la validación.
        verify(usuarioRepository).findById(4L); // Se confirma que el repositorio fue invocado.
    } // Se cierra la prueba negativa de rol válido.

    @Test // Se marca el método como una prueba unitaria.
    void save_debePersistirUsuarioCompleto() { // Se valida que el servicio guarde usuarios completos.
        Usuario usuario = crearUsuarioCompleto("ADMIN"); // Se construye un usuario válido para persistencia.
        when(usuarioRepository.save(usuario)).thenReturn(usuario); // Se simula que el repositorio devuelve el mismo usuario guardado.
        Usuario guardado = usuarioService.save(usuario); // Se ejecuta la operación de guardado del servicio.
        assertSame(usuario, guardado); // Se confirma que el objeto devuelto es el mismo que se envió.
        verify(usuarioRepository).save(usuario); // Se verifica que el servicio delegó la persistencia al repositorio.
    } // Se cierra la prueba de persistencia exitosa.

    @Test // Se marca el método como una prueba unitaria.
    void save_debeLanzarExcepcionSiFaltanDatos() { // Se valida que un usuario incompleto no pueda persistirse.
        Usuario usuario = crearUsuarioCompleto("ADMIN"); // Se crea un usuario de base.
        usuario.setDireccion(null); // Se elimina la dirección para forzar un caso inválido.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.save(usuario)); // Se confirma que el servicio rechaza el registro.
        assertEquals("El usuario debe tener nombre, apellido, email, direccion, username, password y roles.", exception.getMessage()); // Se valida el mensaje informativo de la excepción.
        verify(usuarioRepository, never()).save(any()); // Se verifica que el repositorio no reciba la solicitud de persistencia.
    } // Se cierra la prueba de rechazo por datos incompletos.

    private Usuario crearUsuarioCompleto(String nombreRol) { // Se define un método auxiliar para construir usuarios de prueba.
        Usuario usuario = new Usuario(); // Se crea una instancia vacía de Usuario.
        usuario.setId(1L); // Se asigna un identificador para facilitar la simulación.
        usuario.setNombre("Raul"); // Se asigna el nombre obligatorio.
        usuario.setApellido("Zuniga"); // Se asigna el apellido obligatorio.
        usuario.setEmail("raul.zuniga@minimarket.cl"); // Se asigna el correo obligatorio.
        usuario.setDireccion("Chile 123"); // Se asigna la dirección obligatoria.
        usuario.setUsername("raul"); // Se asigna el nombre de usuario.
        usuario.setPassword("clave123"); // Se asigna la contraseña del usuario.
        usuario.setRoles(Set.of(new Rol(nombreRol))); // Se asigna un rol al usuario para completar la validación.
        return usuario; // Se devuelve el usuario construido.
    } // Se cierra el método auxiliar de creación de usuarios.
} // Se cierra la clase de pruebas.
