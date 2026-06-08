package com.bolao.copa2026.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.model.Selecao;
import com.bolao.copa2026.service.SelecaoService;

@RestController
@RequestMapping("/selecoes")
@PreAuthorize("hasRole('ADMIN')")
public class SelecaoController {

    private final SelecaoService selecaoService;

    public SelecaoController(SelecaoService selecaoService) {
        this.selecaoService = selecaoService;
    }

    @PostMapping
    public ResponseEntity<Selecao> criar(@RequestBody Selecao selecao) {

        Selecao selecaoCriada = selecaoService.criar(selecao);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(selecaoCriada);
    }

    @GetMapping
    public ResponseEntity<List<Selecao>> listarTodos() {
        return ResponseEntity.ok(selecaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Selecao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(selecaoService.buscarPorId(id));
    }

    @GetMapping("/sigla/{siglaFifa}")
    public ResponseEntity<Selecao> buscarPorSiglaFifa(
            @PathVariable String siglaFifa) {

        return ResponseEntity.ok(
                selecaoService.buscarPorSiglaFifa(siglaFifa)
        );
    }
}