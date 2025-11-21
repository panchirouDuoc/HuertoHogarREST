package com.example.HuertoHogar.controller;

import com.example.HuertoHogar.model.Producto;
import com.example.HuertoHogar.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Producto Controller", description = "Operaciones relacionadas con productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Obtiene una lista de todos los productos disponibles")
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID", description = "Obtiene un producto por su ID")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Producto producto = productoService.getProductoById(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto")
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.saveProducto(producto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente", description = "Modifica los datos de un producto según su ID")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        Producto existingProducto = productoService.getProductoById(id);
        if (existingProducto == null) {
            return ResponseEntity.notFound().build();
        }

        existingProducto.setNombre(productoDetails.getNombre());
        existingProducto.setPrecio(productoDetails.getPrecio());
        existingProducto.setDescripcion(productoDetails.getDescripcion());
        existingProducto.setCategoria(productoDetails.getCategoria());
        existingProducto.setImagen(productoDetails.getImagen());
        
        final Producto updatedProducto = productoService.saveProducto(existingProducto);
        return ResponseEntity.ok(updatedProducto);
    }
        
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto por ID", description = "Elimina un producto por su ID")
    public void deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
    }
}
