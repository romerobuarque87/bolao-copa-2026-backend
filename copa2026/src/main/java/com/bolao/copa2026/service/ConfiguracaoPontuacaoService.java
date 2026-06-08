package com.bolao.copa2026.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.model.ConfiguracaoPontuacao;
import com.bolao.copa2026.repository.ConfiguracaoPontuacaoRepository;

@Service
public class ConfiguracaoPontuacaoService {

    private final ConfiguracaoPontuacaoRepository configuracaoPontuacaoRepository;

    public ConfiguracaoPontuacaoService(ConfiguracaoPontuacaoRepository configuracaoPontuacaoRepository) {
        this.configuracaoPontuacaoRepository = configuracaoPontuacaoRepository;
    }

    public ConfiguracaoPontuacao buscarConfiguracao() {
        List<ConfiguracaoPontuacao> configuracoes = configuracaoPontuacaoRepository.findAll();

        if (configuracoes.isEmpty()) {
            ConfiguracaoPontuacao configuracaoPadrao = new ConfiguracaoPontuacao();
            return configuracaoPontuacaoRepository.save(configuracaoPadrao);
        }

        return configuracoes.get(0);
    }

    public ConfiguracaoPontuacao atualizarConfiguracao(ConfiguracaoPontuacao novaConfiguracao) {
        ConfiguracaoPontuacao configuracaoAtual = buscarConfiguracao();

        configuracaoAtual.setPontosPlacarExato(novaConfiguracao.getPontosPlacarExato());
        configuracaoAtual.setPontosResultado(novaConfiguracao.getPontosResultado());
        configuracaoAtual.setPontosGolsMandante(novaConfiguracao.getPontosGolsMandante());
        configuracaoAtual.setPontosGolsVisitante(novaConfiguracao.getPontosGolsVisitante());

        return configuracaoPontuacaoRepository.save(configuracaoAtual);
    }
}