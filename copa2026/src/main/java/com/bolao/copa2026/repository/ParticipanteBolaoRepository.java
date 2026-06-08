package com.bolao.copa2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolao.copa2026.model.Bolao;
import com.bolao.copa2026.model.ParticipanteBolao;
import com.bolao.copa2026.model.Usuario;

public interface ParticipanteBolaoRepository extends JpaRepository<ParticipanteBolao, Long> {

    boolean existsByUsuarioAndBolao(Usuario usuario, Bolao bolao);

    List<ParticipanteBolao> findByBolaoId(Long bolaoId);

    List<ParticipanteBolao> findByUsuarioId(Long usuarioId);

    List<ParticipanteBolao> findByBolaoIdOrderByPontosDesc(Long bolaoId);
}