package com.example.HuertoHogar.service;

import com.example.HuertoHogar.model.FormularioContacto;
import com.example.HuertoHogar.repository.FormularioContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormularioContactoService {

    @Autowired
    private FormularioContactoRepository formularioContactoRepository;

    public FormularioContacto guardarMensaje(FormularioContacto formulario) {
        
        return formularioContactoRepository.save(formulario);
    }

    public List<FormularioContacto> obtenerTodosLosMensajes() {
        return formularioContactoRepository.findAll();
    }

    public void eliminarMensaje(Long id) {
        formularioContactoRepository.deleteById(id);
    }
}
