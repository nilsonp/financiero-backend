package com.prueba.tecnica.financiero.controller;

import com.prueba.tecnica.financiero.dto.ProductoFinancieroDTO;
import com.prueba.tecnica.financiero.service.ProductoFinancieroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/productos-financieros")
@RequiredArgsConstructor
@Tag(name = "Productos Financieros", description = "API para gestionar productos financieros")
public class ProductoFinancieroController {

    private final ProductoFinancieroService productoFinancieroService;

    @Operation(summary = "Obtener todos los productos financieros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos financieros obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<ProductoFinancieroDTO>> obtenerTodosLosProductosFinancieros() {
        return ResponseEntity.ok(productoFinancieroService.buscarTodos());
    }

    @Operation(summary = "Obtener un producto financiero por número de producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto financiero obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto financiero no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoFinancieroDTO> obtenerProductoFinancieroPorId(@PathVariable BigInteger id) {
        return ResponseEntity.ok(productoFinancieroService.buscarPorNumeroProducto(id));
    }

    @Operation(summary = "Crear un nuevo producto financiero")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto financiero creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ProductoFinancieroDTO> crearProductoFinanciero(@RequestBody ProductoFinancieroDTO productoFinancieroDTO) {
        return new ResponseEntity<>(productoFinancieroService.crear(productoFinancieroDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un producto financiero existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto financiero actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto financiero no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoFinancieroDTO> actualizarProductoFinanciero(
            @PathVariable BigInteger id,
            @RequestBody ProductoFinancieroDTO productoFinancieroDTO) {
        return ResponseEntity.ok(productoFinancieroService.actualizar(productoFinancieroDTO, id));
    }

    @Operation(summary = "Eliminar un producto financiero")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto financiero eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto financiero no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoFinanciero(@PathVariable BigInteger id) {
        productoFinancieroService.borrarPorNumeroProducto(id);
        return ResponseEntity.noContent().build();
    }
}
