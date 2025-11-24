package com.example.HuertoHogar.controller;

import com.example.HuertoHogar.model.Resena;
import com.example.HuertoHogar.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@Tag(name = "Reseñas", description = "Operaciones para las reseñas de clientes")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    @Operation(summary = "Obtener todas las reseñas", description = "Devuelve una lista de todas las reseñas, ordenadas por fecha descendente.")
    public ResponseEntity<List<Resena>> obtenerResenas() {
        return ResponseEntity.ok(resenaService.obtenerTodasLasResenas());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reseña", description = "Endpoint público para que los usuarios envíen sus reseñas.")
    public ResponseEntity<Resena> crearResena(@RequestBody Resena resena) {
        Resena nuevaResena = resenaService.guardarResena(resena);
        return new ResponseEntity<>(nuevaResena, HttpStatus.CREATED);
    }
}
