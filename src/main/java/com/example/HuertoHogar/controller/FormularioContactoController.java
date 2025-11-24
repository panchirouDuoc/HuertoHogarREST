package com.example.HuertoHogar.controller;

import com.example.HuertoHogar.model.FormularioContacto;
import com.example.HuertoHogar.service.FormularioContactoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formularios")
@Tag(name = "Formulario de Contacto", description = "Operaciones para el formulario de contacto")
public class FormularioContactoController {

    @Autowired
    private FormularioContactoService formularioContactoService;

    @PostMapping
    @Operation(summary = "Enviar un mensaje de contacto", description = "Endpoint para que los usuarios envíen mensajes.")
    public ResponseEntity<FormularioContacto> recibirFormulario(@RequestBody FormularioContacto formulario) {
        FormularioContacto nuevoMensaje = formularioContactoService.guardarMensaje(formulario);
        return new ResponseEntity<>(nuevoMensaje, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los mensajes (Solo Admin)", description = "Devuelve todos los mensajes enviados al formulario.")
    public ResponseEntity<List<FormularioContacto>> obtenerMensajes() {
        return ResponseEntity.ok(formularioContactoService.obtenerTodosLosMensajes());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un mensaje (Solo Admin)", description = "Elimina un mensaje por su ID.")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long id) {
        formularioContactoService.eliminarMensaje(id);
        return ResponseEntity.noContent().build();
    }
}
