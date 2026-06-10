package com.bolao.copa2026.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.service.MataMataService;

@RestController
@RequestMapping("/mata-mata")
public class MataMataController {

    private final MataMataService mataMataService;

    public MataMataController(MataMataService mataMataService) {
        this.mataMataService = mataMataService;
    }

    @PostMapping("/gerar-dezesseis-avos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> gerarDezesseisAvos() {
        return ResponseEntity.ok(mataMataService.gerarDezesseisAvos());
    }

    @PostMapping("/gerar-oitavas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> gerarOitavas() {
        return ResponseEntity.ok(mataMataService.gerarOitavas());
    }

    @PostMapping("/gerar-quartas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> gerarQuartas() {
        return ResponseEntity.ok(mataMataService.gerarQuartas());
    }

    @PostMapping("/gerar-semifinal")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> gerarSemifinal() {
        return ResponseEntity.ok(mataMataService.gerarSemifinal());
    }

    @PostMapping("/gerar-terceiro-lugar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> gerarTerceiroLugar() {
        return ResponseEntity.ok(mataMataService.gerarTerceiroLugar());
    }

    @PostMapping("/gerar-final")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> gerarFinal() {
        return ResponseEntity.ok(mataMataService.gerarFinal());
    }
}