package com.example.HuertoHogar.service;

import com.example.HuertoHogar.model.Carrito;
import com.example.HuertoHogar.model.CarritoItem;
import com.example.HuertoHogar.model.Producto;
import com.example.HuertoHogar.repository.CarritoRepository;
import com.example.HuertoHogar.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Carrito getCarritoByUsuarioId(Long idUsuario) {
        return carritoRepository.findByIdUsuario(idUsuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito(idUsuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    public Carrito agregarProductoAlCarrito(Long idUsuario, Long idProducto, Integer cantidad) {
        Carrito carrito = getCarritoByUsuarioId(idUsuario);
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + idProducto));

        Optional<CarritoItem> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getIdProducto().equals(idProducto))
                .findFirst();

        if (itemExistente.isPresent()) {
            CarritoItem item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            item.setSubtotal(item.getCantidad() * item.getPrecio());
        } else {
            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setIdProducto(idProducto);
            nuevoItem.setNombreProducto(producto.getNombre());
            nuevoItem.setPrecio(producto.getPrecio());
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setSubtotal(cantidad * producto.getPrecio());
            carrito.getItems().add(nuevoItem);
        }

        recalcularTotal(carrito);
        return carritoRepository.save(carrito);
    }

    public Carrito actualizarCantidadProducto(Long idUsuario, Long idProducto, Integer cantidad) {
        Carrito carrito = getCarritoByUsuarioId(idUsuario);
        CarritoItem item = carrito.getItems().stream()
                .filter(i -> i.getIdProducto().equals(idProducto))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

        if (cantidad <= 0) {
            carrito.getItems().remove(item);
        } else {
            item.setCantidad(cantidad);
            item.setSubtotal(cantidad * item.getPrecio());
        }

        recalcularTotal(carrito);
        return carritoRepository.save(carrito);
    }

    public Carrito eliminarProductoDelCarrito(Long idUsuario, Long idProducto) {
        Carrito carrito = getCarritoByUsuarioId(idUsuario);
        boolean removed = carrito.getItems().removeIf(item -> item.getIdProducto().equals(idProducto));

        if (!removed) {
            throw new RuntimeException("Producto no encontrado en el carrito");
        }

        recalcularTotal(carrito);
        return carritoRepository.save(carrito);
    }

    public Carrito limpiarCarrito(Long idUsuario) {
        Carrito carrito = getCarritoByUsuarioId(idUsuario);
        carrito.getItems().clear();
        carrito.setTotal(0);
        return carritoRepository.save(carrito);
    }

    private void recalcularTotal(Carrito carrito) {
        double total = carrito.getItems().stream()
                .mapToDouble(CarritoItem::getSubtotal)
                .sum();
        carrito.setTotal((int) total);
    }
}
