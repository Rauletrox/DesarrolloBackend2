package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.service.CarritoService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Endpoints para gestionar los productos agregados al carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    @Operation(summary = "Listar carrito", description = "Obtiene todos los items del carrito")
    public CollectionModel<EntityModel<Carrito>> listarCarrito() {
        List<EntityModel<Carrito>> items = carritoService.findAll().stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(items,
                linkTo(methodOn(CarritoController.class).listarCarrito()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item del carrito por ID", description = "Retorna un item específico del carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    public ResponseEntity<EntityModel<Carrito>> obtenerCarritoPorId(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        return (carrito != null)
                ? ResponseEntity.ok(toModel(carrito))
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Agregar producto al carrito", description = "Registra un nuevo item en el carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item agregado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Carrito>> agregarProductoAlCarrito(@RequestBody Carrito carrito) {
        Carrito guardado = carritoService.save(carrito);
        return ResponseEntity.ok(toModel(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar carrito", description = "Modifica un item existente del carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item actualizado"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    public ResponseEntity<EntityModel<Carrito>> actualizarCarrito(@PathVariable Long id, @RequestBody Carrito carrito) {
        Carrito existente = carritoService.findById(id);
        if (existente != null) {
            carrito.setId(id);
            return ResponseEntity.ok(toModel(carritoService.save(carrito)));
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

    private EntityModel<Carrito> toModel(Carrito carrito) {
        EntityModel<Carrito> model = EntityModel.of(carrito);
        model.add(linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(carrito.getId())).withSelfRel());
        model.add(linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito"));
        return model;
    }
}
