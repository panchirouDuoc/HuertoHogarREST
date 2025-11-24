package com.example.HuertoHogar.service;

import com.example.HuertoHogar.model.Resena;
import com.example.HuertoHogar.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public List<Resena> obtenerTodasLasResenas() {
        return resenaRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
    }

    public Resena guardarResena(Resena resena) {
        return resenaRepository.save(resena);
    }
}
