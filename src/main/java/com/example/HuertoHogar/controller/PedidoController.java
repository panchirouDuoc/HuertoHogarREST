package com.example.HuertoHogar.controller;

import com.example.HuertoHogar.model.EstadoPedido;
import com.example.HuertoHogar.model.Pedido;
import com.example.HuertoHogar.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedido Controller", description = "Operaciones relacionadas con pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo pedido desde el carrito", description = "Crea un pedido a partir de los items del carrito del usuario y lo vacía.")
    public ResponseEntity<Pedido> crearPedido(@RequestBody Map<String, Long> request) {
        Long idUsuario = request.get("idUsuario");
        if (idUsuario == null) {
            return ResponseEntity.badRequest().build();
        }
        Pedido nuevoPedido = pedidoService.crearPedido(idUsuario);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @GetMapping("/{idPedido}")
    @Operation(summary = "Obtener un pedido por su ID", description = "Devuelve los detalles de un pedido específico.")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long idPedido) {
        Pedido pedido = pedidoService.getPedidoById(idPedido);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Obtener todos los pedidos de un usuario", description = "Devuelve una lista de todos los pedidos realizados por un usuario.")
    public ResponseEntity<List<Pedido>> getPedidosByUsuario(@PathVariable Long idUsuario) {
        List<Pedido> pedidos = pedidoService.getPedidosByUsuarioId(idUsuario);
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{idPedido}/estado")
    @Operation(summary = "Actualizar el estado de un pedido (Solo Admin)", description = "Permite a un administrador cambiar el estado de un pedido (ej. PENDIENTE a ENVIADO).")
    //ruta protegida 
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long idPedido,
            @RequestBody Map<String, String> request) {
        EstadoPedido nuevoEstado = EstadoPedido.valueOf(request.get("estado").toUpperCase());
        Pedido pedidoActualizado = pedidoService.actualizarEstadoPedido(idPedido, nuevoEstado);
        return ResponseEntity.ok(pedidoActualizado);
    }
}
