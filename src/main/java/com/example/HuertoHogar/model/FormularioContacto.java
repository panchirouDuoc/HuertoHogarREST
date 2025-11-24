package com.example.HuertoHogar.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "formularios_contacto")
@Data
@NoArgsConstructor
public class FormularioContacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    @Column(columnDefinition = "TEXT")
    private String mensaje;
    private LocalDateTime fechaEnvio;

    @PrePersist
    protected void onSend() { fechaEnvio = LocalDateTime.now(); }
}

