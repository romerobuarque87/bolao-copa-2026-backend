package com.bolao.copa2026.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.model.ConfiguracaoPontuacao;
import com.bolao.copa2026.service.ConfiguracaoPontuacaoService;

@RestController
@RequestMapping("/configuracao-pontuacao")
@CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')")
public class ConfiguracaoPontuacaoController {

    private final ConfiguracaoPontuacaoService configuracaoPontuacaoService;

    public ConfiguracaoPontuacaoController(
            ConfiguracaoPontuacaoService configuracaoPontuacaoService) {

        this.configuracaoPontuacaoService = configuracaoPontuacaoService;
    }

    @GetMapping
    public ConfiguracaoPontuacao buscarConfiguracao() {
        return configuracaoPontuacaoService.buscarConfiguracao();
    }

    @PutMapping
    public ConfiguracaoPontuacao atualizarConfiguracao(
            @RequestBody ConfiguracaoPontuacao configuracaoPontuacao) {

        return configuracaoPontuacaoService
                .atualizarConfiguracao(configuracaoPontuacao);
    }
}