package com.bolao.copa2026.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolao.copa2026.model.Jogo;
import com.bolao.copa2026.model.Palpite;
import com.bolao.copa2026.model.ParticipanteBolao;

public interface PalpiteRepository extends JpaRepository<Palpite, Long> {

    List<Palpite> findByJogo(Jogo jogo);

    List<Palpite> findByParticipanteBolao(
            ParticipanteBolao participanteBolao
    );

    Optional<Palpite> findByParticipanteBolaoAndJogo(
            ParticipanteBolao participanteBolao,
            Jogo jogo
    );

    boolean existsByParticipanteBolaoAndJogo(
            ParticipanteBolao participanteBolao,
            Jogo jogo
    );

    long countByParticipanteBolaoAndJogo_FinalizadoFalse(
            ParticipanteBolao participanteBolao
    );

    List<Palpite> findByParticipanteBolao_Bolao_IdAndParticipanteBolao_PalpitesEnviadosTrueOrderByParticipanteBolao_Usuario_NomeAscJogo_DataHoraAsc(
            Long bolaoId
    );
}