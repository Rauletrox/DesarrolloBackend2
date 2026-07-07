package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Endpoints para gestionar los productos agregados al carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    @Operation(summary = "Listar carrito", description = "Obtiene todos los items del carrito")
    public List<Carrito> listarCarrito() {
        return carritoService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item del carrito por ID", description = "Retorna un item específico del carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    public ResponseEntity<Carrito> obtenerCarritoPorId(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        return (carrito != null) ? ResponseEntity.ok(carrito) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Agregar producto al carrito", description = "Registra un nuevo item en el carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item agregado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public Carrito agregarProductoAlCarrito(@RequestBody Carrito carrito) {
        return carritoService.save(carrito);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar carrito", description = "Modifica un item existente del carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item actualizado"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    public ResponseEntity<Carrito> actualizarCarrito(@PathVariable Long id, @RequestBody Carrito carrito) {
        Carrito existente = carritoService.findById(id);
        if (existente != null) {
            carrito.setId(id);
            return ResponseEntity.ok(carritoService.save(carrito));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar item del carrito", description = "Elimina un item del carrito por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item eliminado"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito != null) {
            carritoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
