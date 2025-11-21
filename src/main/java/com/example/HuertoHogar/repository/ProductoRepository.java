package com.example.HuertoHogar.repository;

import com.example.HuertoHogar.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}