package com.example.HuertoHogar.controller;

import com.example.HuertoHogar.model.Carrito;
import com.example.HuertoHogar.model.Usuario;
import com.example.HuertoHogar.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito Controller", description = "Operaciones para gestionar el carrito de compras")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    @Operation(summary = "Obtener el carrito del usuario autenticado", description = "Devuelve el carrito de compras del usuario que realiza la petición.")
    public ResponseEntity<Carrito> getCarrito(@AuthenticationPrincipal Usuario usuario) {
        Carrito carrito = carritoService.getCarritoByUsuarioId(usuario.getId());
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/agregar")
    @Operation(summary = "Agregar un producto al carrito", description = "Añade un producto con una cantidad específica al carrito del usuario.")
    public ResponseEntity<Carrito> agregarProducto(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody Map<String, Object> payload) {
        Long idProducto = Long.valueOf(payload.get("idProducto").toString());
        Integer cantidad = Integer.valueOf(payload.get("cantidad").toString());
        Carrito carrito = carritoService.agregarProductoAlCarrito(usuario.getId(), idProducto, cantidad);
        return ResponseEntity.ok(carrito);
    }

    @PutMapping("/producto/{idProducto}")
    @Operation(summary = "Actualizar la cantidad de un producto", description = "Modifica la cantidad de un producto existente en el carrito.")
    public ResponseEntity<Carrito> actualizarCantidad(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long idProducto,
            @RequestBody Map<String, Integer> payload) {
        Integer cantidad = payload.get("cantidad");
        Carrito carrito = carritoService.actualizarCantidadProducto(usuario.getId(), idProducto, cantidad);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/producto/{idProducto}")
    @Operation(summary = "Eliminar un producto del carrito", description = "Quita un producto específico del carrito del usuario.")
    public ResponseEntity<Carrito> eliminarProducto(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long idProducto) {
        Carrito carrito = carritoService.eliminarProductoDelCarrito(usuario.getId(), idProducto);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping
    @Operation(summary = "Vaciar el carrito", description = "Elimina todos los productos del carrito del usuario.")
    public ResponseEntity<Carrito> limpiarCarrito(@AuthenticationPrincipal Usuario usuario) {
        Carrito carrito = carritoService.limpiarCarrito(usuario.getId());
        return ResponseEntity.ok(carrito);
    }
}
