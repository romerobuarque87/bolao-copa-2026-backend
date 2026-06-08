package com.bolao.copa2026.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.dto.RankingResponseDTO;
import com.bolao.copa2026.model.ParticipanteBolao;
import com.bolao.copa2026.repository.ParticipanteBolaoRepository;

@Service
public class RankingService {

    private final ParticipanteBolaoRepository participanteBolaoRepository;

    public RankingService(ParticipanteBolaoRepository participanteBolaoRepository) {
        this.participanteBolaoRepository = participanteBolaoRepository;
    }

    public List<RankingResponseDTO> buscarRankingPorBolao(Long bolaoId) {
        List<ParticipanteBolao> participantes =
                participanteBolaoRepository.findByBolaoIdOrderByPontosDesc(bolaoId);

        List<RankingResponseDTO> ranking = new ArrayList<>();

        int posicao = 1;

        for (ParticipanteBolao participante : participantes) {
            RankingResponseDTO response = new RankingResponseDTO();

            response.setPosicao(posicao);
            response.setParticipanteBolaoId(participante.getId());
            response.setUsuarioId(participante.getUsuario().getId());
            response.setNomeUsuario(participante.getUsuario().getNome());
            response.setBolaoId(participante.getBolao().getId());
            response.setNomeBolao(participante.getBolao().getNome());
            response.setPontos(participante.getPontos());

            ranking.add(response);
            posicao++;
        }

        return ranking;
    }
}