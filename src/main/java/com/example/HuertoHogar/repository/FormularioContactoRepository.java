package com.example.HuertoHogar.repository;

import com.example.HuertoHogar.model.FormularioContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormularioContactoRepository extends JpaRepository<FormularioContacto, Long> {}
