package com.bolao.copa2026.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolao.copa2026.model.Selecao;

public interface SelecaoRepository extends JpaRepository<Selecao, Long> {

    Optional<Selecao> findBySiglaFifa(String siglaFifa);

    List<Selecao> findByGrupoOrderByNomeAsc(String grupo);
}