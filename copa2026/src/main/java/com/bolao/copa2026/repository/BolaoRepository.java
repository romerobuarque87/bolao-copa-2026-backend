package com.bolao.copa2026.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolao.copa2026.model.Bolao;

public interface BolaoRepository extends JpaRepository<Bolao, Long> {

    Optional<Bolao> findByCodigoConvite(String codigoConvite);
}