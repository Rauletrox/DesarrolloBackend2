package com.minimarket.service.impl;

import com.minimarket.entity.Categoria; 
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

 // Se activa la integración entre JUnit 5 y Mockito.
@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest { // Se declara la clase de pruebas del servicio de ventas.

    @Mock // Se crea un mock del repositorio de ventas.
    private VentaRepository ventaRepository; // Se declara el repositorio falso que devolverá respuestas controladas.

    @Mock // Se crea un mock del servicio de productos.
    private ProductoService productoService; // Se declara el servicio simulado que permitirá revisar stock.

    @InjectMocks // Se inyectan los mocks en la clase real del servicio de ventas.
    private VentaServiceImpl ventaService; // Se crea la instancia bajo prueba con dependencias simuladas.

    @Test // Se marca el método como una prueba unitaria.
    void tieneStockSuficiente_debeRetornarTrueCuandoElStockAlcanza() { // Se valida que el stock suficiente permita la venta.
        Venta venta = crearVentaConDetalle(1L, 3, 1500.0); // Se construye una venta con una cantidad menor al stock disponible.
        Producto producto = crearProducto(1L, 1500.0, 10); // Se crea un producto con stock suficiente para la prueba.
        when(productoService.findById(1L)).thenReturn(producto); // Se simula la consulta al servicio de productos.
        boolean resultado = ventaService.tieneStockSuficiente(venta); // Se ejecuta la validación de stock del servicio.
        assertTrue(resultado); // Se confirma que la venta puede procesarse.
        verify(productoService).findById(1L); // Se verifica que se consultó el producto en la capa simulada.
    } // Se cierra la prueba positiva de stock.

    @Test // Se marca el método como una prueba unitaria.
    void tieneStockSuficiente_debeRetornarFalseCuandoNoAlcanzaElStock() { // Se valida que una venta excedida sea rechazada.
        Venta venta = crearVentaConDetalle(2L, 5, 1500.0); // Se construye una venta que solicita más unidades de las disponibles.
        Producto producto = crearProducto(2L, 1500.0, 2); // Se crea un producto con stock insuficiente.
        when(productoService.findById(2L)).thenReturn(producto); // Se simula la consulta al servicio de productos.
        boolean resultado = ventaService.tieneStockSuficiente(venta); // Se ejecuta la verificación de stock.
        assertFalse(resultado); // Se confirma que la venta no cumple con el stock mínimo.
        verify(productoService).findById(2L); // Se verifica que la dependencia fue consultada.
    } // Se cierra la prueba negativa de stock.

    @Test // Se marca el método como una prueba unitaria.
    void tieneStockSuficiente_debeRetornarFalseCuandoLaVentaEsNula() { // Se valida que una venta nula sea rechazada.
        boolean resultado = ventaService.tieneStockSuficiente(null); // Se ejecuta la validación con una referencia nula.
        assertFalse(resultado); // Se confirma que el servicio no acepta la venta inexistente.
        verify(productoService, never()).findById(any()); // Se confirma que no se consulta stock si la venta es nula.
    } // Se cierra la prueba de venta nula.

    @Test // Se marca el método como una prueba unitaria.
    void calcularTotal_debeSumarCorrectamenteLosDetalles() { // Se valida que el total se calcule sumando los subtotales de cada detalle.
        Venta venta = crearVentaConDosDetalles(); // Se construye una venta con dos productos distintos.
        double total = ventaService.calcularTotal(venta); // Se ejecuta el cálculo de total del servicio.
        assertEquals(6500.0, total); // Se confirma que el total coincide con la suma esperada.
    } // Se cierra la prueba de cálculo total.

    @Test // Se marca el método como una prueba unitaria.
    void calcularTotal_debeRetornarCeroCuandoLaVentaEsNula() { // Se valida el comportamiento del cálculo cuando no hay venta.
        double total = ventaService.calcularTotal(null); // Se ejecuta el cálculo con una referencia nula.
        assertEquals(0.0, total); // Se confirma que el total por defecto es cero.
    } // Se cierra la prueba de total nulo.

    @Test // Se marca el método como una prueba unitaria.
    void findAll_debeRetornarTodasLasVentasRegistradas() { // Se valida que el servicio delegue la búsqueda total al repositorio.
        Venta venta = crearVentaConDetalle(5L, 1, 1000.0); // Se crea una venta de ejemplo para la lista simulada.
        when(ventaRepository.findAll()).thenReturn(List.of(venta)); // Se simula el resultado del repositorio.
        List<Venta> ventas = ventaService.findAll(); // Se ejecuta la consulta del servicio.
        assertEquals(1, ventas.size()); // Se confirma que el servicio devuelve una sola venta.
        assertSame(venta, ventas.get(0)); // Se confirma que la venta devuelta es la misma instancia simulada.
        verify(ventaRepository).findAll(); // Se verifica la invocación al repositorio.
    } // Se cierra la prueba de listado general.

    @Test // Se marca el método como una prueba unitaria.
    void findById_debeRetornarVentaCuandoElIdExiste() { // Se valida que el servicio recupere una venta por identificador.
        Venta venta = crearVentaConDetalle(6L, 1, 1200.0); // Se construye una venta de ejemplo.
        when(ventaRepository.findById(6L)).thenReturn(java.util.Optional.of(venta)); // Se simula el hallazgo de la venta en el repositorio.
        Venta resultado = ventaService.findById(6L); // Se ejecuta la búsqueda por id.
        assertSame(venta, resultado); // Se confirma que la venta encontrada coincide con la simulada.
        verify(ventaRepository).findById(6L); // Se verifica que el repositorio fue consultado.
    } // Se cierra la prueba de búsqueda por id.

    @Test // Se marca el método como una prueba unitaria.
    void findByUsuarioId_debeRetornarVentasDelUsuario() { // Se valida que el servicio delegue la búsqueda por usuario.
        Venta venta = crearVentaConDetalle(7L, 2, 1300.0); // Se crea una venta de ejemplo asociada a un usuario.
        when(ventaRepository.findByUsuarioId(1L)).thenReturn(List.of(venta)); // Se simula la respuesta del repositorio por usuario.
        List<Venta> ventas = ventaService.findByUsuarioId(1L); // Se ejecuta la búsqueda por usuario.
        assertEquals(1, ventas.size()); // Se confirma que el servicio devuelve una venta.
        assertSame(venta, ventas.get(0)); // Se confirma que la instancia coincide con la simulada.
        verify(ventaRepository).findByUsuarioId(1L); // Se verifica la interacción con el repositorio.
    } // Se cierra la prueba de búsqueda por usuario.

    @Test // Se marca el método como una prueba unitaria.
    void save_debePersistirVentaCuandoHayStock() { // Se valida que la venta se guarde cuando hay stock suficiente.
        Venta venta = crearVentaConDetalle(3L, 2, 2000.0); // Se construye una venta válida para persistencia.
        Producto producto = crearProducto(3L, 2000.0, 8); // Se crea el producto con stock suficiente.
        when(productoService.findById(3L)).thenReturn(producto); // Se simula la búsqueda del producto en el servicio dependiente.
        when(ventaRepository.save(venta)).thenReturn(venta); // Se simula el guardado exitoso en el repositorio.
        Venta guardada = ventaService.save(venta); // Se ejecuta el guardado real del servicio.
        assertSame(venta, guardada); // Se confirma que el servicio devuelve la misma instancia persistida.
        verify(productoService).findById(3L); // Se verifica que se revisó el stock antes de guardar.
        verify(ventaRepository).save(venta); // Se confirma que el repositorio recibió la orden de persistencia.
    } // Se cierra la prueba de persistencia exitosa.

    @Test // Se marca el método como una prueba unitaria.
    void save_debeLanzarExcepcionCuandoNoHayStock() { // Se valida que la venta no se guarde si el stock es insuficiente.
        Venta venta = crearVentaConDetalle(4L, 9, 1200.0); // Se construye una venta que excede el stock.
        Producto producto = crearProducto(4L, 1200.0, 1); // Se crea un producto con stock menor al solicitado.
        when(productoService.findById(4L)).thenReturn(producto); // Se simula la consulta del producto en la capa de datos.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ventaService.save(venta)); // Se confirma que el servicio rechaza la operación.
        assertEquals("No es posible registrar la venta porque uno o más productos no tienen stock suficiente.", exception.getMessage()); // Se valida el mensaje de la excepción.
        verify(productoService).findById(4L); // Se verifica que el stock fue revisado.
        verify(ventaRepository, never()).save(any()); // Se confirma que el repositorio no recibió la venta inválida.
    } // Se cierra la prueba de rechazo por stock insuficiente.

    private Producto crearProducto(Long id, Double precio, Integer stock) { // Se define un método auxiliar para construir productos de prueba.
        Producto producto = new Producto(); // Se crea una instancia vacía de Producto.
        producto.setId(id); // Se asigna el identificador del producto.
        producto.setNombre("Producto " + id); // Se asigna un nombre de ejemplo.
        producto.setPrecio(precio); // Se asigna el precio del producto.
        producto.setStock(stock); // Se asigna el stock disponible.
        producto.setCategoria(crearCategoria()); // Se asigna una categoría válida para completar la entidad.
        return producto; // Se devuelve el producto construido.
    } // Se cierra el método auxiliar de producto.

    private Categoria crearCategoria() { // Se define un método auxiliar para construir la categoría requerida por el producto.
        Categoria categoria = new Categoria(); // Se crea una instancia vacía de Categoría.
        categoria.setId(1L); // Se asigna un identificador de categoría.
        categoria.setNombre("Abarrotes"); // Se asigna un nombre de categoría de ejemplo.
        return categoria; // Se devuelve la categoría construida.
    } // Se cierra el método auxiliar de categoría.

    private Venta crearVentaConDetalle(Long productoId, Integer cantidad, Double precioUnitario) { // Se define un método auxiliar para construir una venta con un solo detalle.
        Venta venta = new Venta(); // Se crea una instancia vacía de Venta.
        venta.setId(1L); // Se asigna un identificador para la venta de prueba.
        venta.setUsuario(crearUsuario()); // Se asocia un usuario válido a la venta.
        venta.setDetalles(List.of(crearDetalle(productoId, cantidad, precioUnitario))); // Se agregan los detalles que serán evaluados por el servicio.
        return venta; // Se devuelve la venta construida.
    } // Se cierra el método auxiliar de venta simple.

    private Venta crearVentaConDosDetalles() { // Se define un método auxiliar para construir una venta con dos líneas de detalle.
        Venta venta = new Venta(); // Se crea una instancia vacía de Venta.
        venta.setId(2L); // Se asigna un identificador distinto para la segunda venta.
        venta.setUsuario(crearUsuario()); // Se asocia el mismo usuario de prueba.
        venta.setDetalles(List.of( // Se construye la lista de detalles de la venta.
                crearDetalle(10L, 2, 1500.0), // Se agrega el primer detalle con subtotal 3000.
                crearDetalle(11L, 1, 3500.0) // Se agrega el segundo detalle con subtotal 3500.
        )); // Se cierra la lista de detalles.
        return venta; // Se devuelve la venta construida.
    } // Se cierra el método auxiliar de venta múltiple.

    private DetalleVenta crearDetalle(Long productoId, Integer cantidad, Double precioUnitario) { // Se define un método auxiliar para construir un detalle de venta.
        DetalleVenta detalle = new DetalleVenta(); // Se crea una instancia vacía de DetalleVenta.
        detalle.setId(productoId); // Se asigna un id técnico al detalle para facilitar el seguimiento.
        detalle.setProducto(crearProducto(productoId, precioUnitario, 99)); // Se asocia un producto con stock alto para no interferir con el cálculo.
        detalle.getProducto().setId(productoId); // Se refuerza el id del producto para que coincida con la búsqueda simulada.
        detalle.setCantidad(cantidad); // Se asigna la cantidad vendida.
        detalle.setPrecio(precioUnitario); // Se asigna el precio unitario usado por el cálculo del total.
        return detalle; // Se devuelve el detalle construido.
    } // Se cierra el método auxiliar de detalle.

    private Usuario crearUsuario() { // Se define un método auxiliar para construir el usuario asociado a la venta.
        Usuario usuario = new Usuario(); // Se crea una instancia vacía de Usuario.
        usuario.setId(1L); // Se asigna un identificador de prueba.
        usuario.setNombre("Raul"); // Se asigna el nombre del usuario.
        usuario.setApellido("Zuniga"); // Se asigna el apellido del usuario.
        usuario.setEmail("raul.zuniga@minimarket.cl"); // Se asigna el correo del usuario.
        usuario.setDireccion("Chile 123"); // Se asigna la dirección del usuario.
        usuario.setUsername("raul"); // Se asigna el nombre de usuario.
        usuario.setPassword("clave123"); // Se asigna la contraseña del usuario.
        return usuario; // Se devuelve el usuario construido.
    } // Se cierra el método auxiliar del usuario.
} // Se cierra la clase de pruebas.
