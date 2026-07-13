package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Endpoints para registrar y consultar movimientos de inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    @Operation(summary = "Listar inventario", description = "Obtiene todos los movimientos de inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public CollectionModel<EntityModel<Inventario>> listarMovimientosDeInventario() {
        List<EntityModel<Inventario>> movimientos = inventarioService.findAll().stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(movimientos,
                linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar movimiento por ID", description = "Retorna un movimiento especifico de inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<EntityModel<Inventario>> obtenerMovimientoPorId(@PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        return (inventario != null)
                ? ResponseEntity.ok(toModel(inventario))
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar movimiento", description = "Registra un nuevo movimiento de inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<EntityModel<Inventario>> registrarMovimiento(@RequestBody Inventario inventario) {
        Inventario guardado = inventarioService.save(inventario);
        return ResponseEntity.ok(toModel(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar movimiento", description = "Modifica un movimiento de inventario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<EntityModel<Inventario>> actualizarMovimiento(@PathVariable Long id, @RequestBody Inventario inventario) {
        Inventario existente = inventarioService.findById(id);
        if (existente != null) {
            inventario.setId(id);
            return ResponseEntity.ok(toModel(inventarioService.save(inventario)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar movimiento", description = "Elimina un movimiento de inventario por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario != null) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private EntityModel<Inventario> toModel(Inventario inventario) {
        EntityModel<Inventario> model = EntityModel.of(inventario);
        model.add(linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(inventario.getId())).withSelfRel());
        model.add(linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withRel("inventario"));
        return model;
    }
}
