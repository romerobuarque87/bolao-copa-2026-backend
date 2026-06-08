package com.bolao.copa2026.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.dto.JogoRequestDTO;
import com.bolao.copa2026.dto.JogoResponseDTO;
import com.bolao.copa2026.service.JogoService;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JogoResponseDTO> criar(@RequestBody JogoRequestDTO request) {
        JogoResponseDTO jogoCriado = jogoService.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jogoCriado);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<JogoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(jogoService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<JogoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(jogoService.buscarPorId(id));
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JogoResponseDTO> finalizarJogo(
            @PathVariable Long id,
            @RequestParam Integer golsCasa,
            @RequestParam Integer golsVisitante) {

        JogoResponseDTO jogoFinalizado =
                jogoService.finalizarJogo(id, golsCasa, golsVisitante);

        return ResponseEntity.ok(jogoFinalizado);
    }

    @PutMapping("/{id}/corrigir-resultado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JogoResponseDTO> corrigirResultado(
            @PathVariable Long id,
            @RequestParam Integer golsCasa,
            @RequestParam Integer golsVisitante) {

        JogoResponseDTO jogoCorrigido =
                jogoService.corrigirResultado(id, golsCasa, golsVisitante);

        return ResponseEntity.ok(jogoCorrigido);
    }
}