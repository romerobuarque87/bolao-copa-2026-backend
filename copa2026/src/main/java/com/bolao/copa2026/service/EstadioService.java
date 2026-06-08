package com.bolao.copa2026.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.exception.ResourceNotFoundException;
import com.bolao.copa2026.model.Estadio;
import com.bolao.copa2026.repository.EstadioRepository;

@Service
public class EstadioService {

    private final EstadioRepository estadioRepository;

    public EstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    public Estadio criar(Estadio estadio) {
        return estadioRepository.save(estadio);
    }

    public List<Estadio> listarTodos() {
        return estadioRepository.findAll();
    }

    public Estadio buscarPorId(Long id) {
        return estadioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estádio não encontrado"));
    }
}