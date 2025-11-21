package com.example.HuertoHogar.service;

import com.example.HuertoHogar.model.*;
import com.example.HuertoHogar.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarritoService carritoService;

    public Pedido crearPedido(Long idUsuario) {
        
        Carrito carrito = carritoService.getCarritoByUsuarioId(idUsuario);

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío. No se puede crear un pedido.");
        }

        // 2. Crear un nuevo objeto Pedido
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(idUsuario);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTotal(carrito.getTotal());

        // 3. Convertir CarritoItems a PedidoItems
        List<PedidoItem> pedidoItems = carrito.getItems().stream()
                .map(carritoItem -> {
                    PedidoItem item = new PedidoItem();
                    item.setIdProducto(carritoItem.getIdProducto());
                    item.setNombreProducto(carritoItem.getNombreProducto());
                    item.setPrecio(carritoItem.getPrecio());
                    item.setCantidad(carritoItem.getCantidad());
                    item.setSubtotal(carritoItem.getSubtotal());
                    return item;
                }).collect(Collectors.toList());

        pedido.setItems(pedidoItems);

        // 4. Guardar el nuevo pedido en la base de datos
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 5. Limpiar el carrito del usuario
        carritoService.limpiarCarrito(idUsuario);

        return pedidoGuardado;
    }

    public Pedido getPedidoById(Long idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));
    }

    public List<Pedido> getPedidosByUsuarioId(Long idUsuario) {
        return pedidoRepository.findByIdUsuario(idUsuario);
    }

    public Pedido actualizarEstadoPedido(Long idPedido, EstadoPedido nuevoEstado) {
        Pedido pedido = getPedidoById(idPedido);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}
