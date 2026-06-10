package com.bolao.copa2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolao.copa2026.model.FaseCopa;
import com.bolao.copa2026.model.Jogo;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

    long countByFinalizadoFalse();

    long countByFase(FaseCopa fase);

    long countByFaseAndFinalizadoFalse(FaseCopa fase);

    List<Jogo> findByFaseOrderByDataHoraAsc(FaseCopa fase);

    List<Jogo> findByGrupoOrderByDataHoraAsc(String grupo);

    List<Jogo> findByFaseAndGrupoOrderByDataHoraAsc(FaseCopa fase, String grupo);
}