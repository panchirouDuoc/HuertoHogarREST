package com.example.HuertoHogar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carrito_items")
@Data 
@NoArgsConstructor 
@AllArgsConstructor

public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private Long idProducto;
    private String nombreProducto;
    private int precio;
    private Integer cantidad;
    private int subtotal;

}
