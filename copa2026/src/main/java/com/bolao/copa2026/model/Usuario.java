package com.bolao.copa2026.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    private String senha;

    private String telefone;

    private Boolean receberNotificacaoEmail = true;

    private Boolean receberNotificacaoWhatsapp = true;

    private Boolean administrador = false;

    private Boolean ativo = true;

    private LocalDateTime dataCadastro = LocalDateTime.now();
}