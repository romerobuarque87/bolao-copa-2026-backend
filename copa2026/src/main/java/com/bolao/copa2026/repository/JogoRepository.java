package com.bolao.copa2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolao.copa2026.model.Jogo;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

    long countByFinalizadoFalse();
}