package com.bolao.copa2026.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.exception.ResourceNotFoundException;
import com.bolao.copa2026.model.Selecao;
import com.bolao.copa2026.repository.SelecaoRepository;

@Service
public class SelecaoService {

    private final SelecaoRepository selecaoRepository;

    public SelecaoService(SelecaoRepository selecaoRepository) {
        this.selecaoRepository = selecaoRepository;
    }

    public Selecao criar(Selecao selecao) {
        return selecaoRepository.save(selecao);
    }

    public List<Selecao> listarTodos() {
        return selecaoRepository.findAll();
    }

    public Selecao buscarPorId(Long id) {
        return selecaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seleção não encontrada"));
    }

    public Selecao buscarPorSiglaFifa(String siglaFifa) {
        return selecaoRepository.findBySiglaFifa(siglaFifa)
                .orElseThrow(() -> new ResourceNotFoundException("Seleção não encontrada"));
    }
}