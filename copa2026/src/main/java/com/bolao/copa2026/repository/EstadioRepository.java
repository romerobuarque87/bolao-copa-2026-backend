package com.bolao.copa2026.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolao.copa2026.model.Estadio;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    Optional<Estadio> findByNome(String nome);
}