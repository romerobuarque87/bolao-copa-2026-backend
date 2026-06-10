package com.bolao.copa2026.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.dto.ClassificacaoGrupoDTO;
import com.bolao.copa2026.model.FaseCopa;
import com.bolao.copa2026.model.Jogo;
import com.bolao.copa2026.model.Selecao;
import com.bolao.copa2026.repository.JogoRepository;
import com.bolao.copa2026.repository.SelecaoRepository;

@Service
public class ClassificacaoService {

    private final SelecaoRepository selecaoRepository;
    private final JogoRepository jogoRepository;

    public ClassificacaoService(
            SelecaoRepository selecaoRepository,
            JogoRepository jogoRepository
    ) {
        this.selecaoRepository = selecaoRepository;
        this.jogoRepository = jogoRepository;
    }

    public Map<String, List<ClassificacaoGrupoDTO>> listarClassificacaoTodosGrupos() {
        Map<String, List<ClassificacaoGrupoDTO>> resultado = new LinkedHashMap<>();

        String[] grupos = {
                "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L"
        };

        for (String grupo : grupos) {
            resultado.put(grupo, calcularClassificacaoGrupo(grupo));
        }

        return resultado;
    }

    public List<ClassificacaoGrupoDTO> calcularClassificacaoGrupo(String grupo) {
        List<Selecao> selecoes = selecaoRepository.findByGrupoOrderByNomeAsc(grupo.toUpperCase());

        Map<Long, ClassificacaoGrupoDTO> tabela = new LinkedHashMap<>();

        for (Selecao selecao : selecoes) {
            ClassificacaoGrupoDTO dto = new ClassificacaoGrupoDTO();
            dto.setSelecaoId(selecao.getId());
            dto.setNomeSelecao(selecao.getNome());
            dto.setSiglaFifa(selecao.getSiglaFifa());
            dto.setBandeiraUrl(selecao.getBandeiraUrl());
            dto.setGrupo(selecao.getGrupo());

            tabela.put(selecao.getId(), dto);
        }

        List<Jogo> jogos = jogoRepository.findByFaseAndGrupoOrderByDataHoraAsc(
                FaseCopa.GRUPOS,
                grupo.toUpperCase()
        );

        for (Jogo jogo : jogos) {
            if (!Boolean.TRUE.equals(jogo.getFinalizado())) {
                continue;
            }

            ClassificacaoGrupoDTO casa = tabela.get(jogo.getTimeCasa().getId());
            ClassificacaoGrupoDTO visitante = tabela.get(jogo.getTimeVisitante().getId());

            if (casa == null || visitante == null) {
                continue;
            }

            int golsCasa = jogo.getGolsCasa();
            int golsVisitante = jogo.getGolsVisitante();

            casa.setJogos(casa.getJogos() + 1);
            visitante.setJogos(visitante.getJogos() + 1);

            casa.setGolsPro(casa.getGolsPro() + golsCasa);
            casa.setGolsContra(casa.getGolsContra() + golsVisitante);

            visitante.setGolsPro(visitante.getGolsPro() + golsVisitante);
            visitante.setGolsContra(visitante.getGolsContra() + golsCasa);

            if (golsCasa > golsVisitante) {
                casa.setVitorias(casa.getVitorias() + 1);
                casa.setPontos(casa.getPontos() + 3);

                visitante.setDerrotas(visitante.getDerrotas() + 1);
            } else if (golsCasa < golsVisitante) {
                visitante.setVitorias(visitante.getVitorias() + 1);
                visitante.setPontos(visitante.getPontos() + 3);

                casa.setDerrotas(casa.getDerrotas() + 1);
            } else {
                casa.setEmpates(casa.getEmpates() + 1);
                visitante.setEmpates(visitante.getEmpates() + 1);

                casa.setPontos(casa.getPontos() + 1);
                visitante.setPontos(visitante.getPontos() + 1);
            }

            casa.setSaldoGols(casa.getGolsPro() - casa.getGolsContra());
            visitante.setSaldoGols(visitante.getGolsPro() - visitante.getGolsContra());
        }

        List<ClassificacaoGrupoDTO> classificacao = new ArrayList<>(tabela.values());

        classificacao.sort(
                Comparator.comparing(ClassificacaoGrupoDTO::getPontos).reversed()
                        .thenComparing(ClassificacaoGrupoDTO::getSaldoGols, Comparator.reverseOrder())
                        .thenComparing(ClassificacaoGrupoDTO::getGolsPro, Comparator.reverseOrder())
                        .thenComparing(ClassificacaoGrupoDTO::getNomeSelecao)
        );

        for (int i = 0; i < classificacao.size(); i++) {
            classificacao.get(i).setPosicao(i + 1);
        }

        return classificacao;
    }
}