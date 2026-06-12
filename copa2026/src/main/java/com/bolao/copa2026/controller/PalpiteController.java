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
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.dto.LiberarAlteracaoPalpiteRequestDTO;
import com.bolao.copa2026.dto.PalpiteAlteracaoRequestDTO;
import com.bolao.copa2026.dto.PalpiteRequestDTO;
import com.bolao.copa2026.dto.PalpiteResponseDTO;
import com.bolao.copa2026.service.PalpiteService;

@RestController
@RequestMapping("/palpites")
public class PalpiteController {

    private final PalpiteService palpiteService;

    public PalpiteController(PalpiteService palpiteService) {
        this.palpiteService = palpiteService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PalpiteResponseDTO> criar(@RequestBody PalpiteRequestDTO request) {
        PalpiteResponseDTO palpiteCriado = palpiteService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(palpiteCriado);
    }

    @PutMapping("/{palpiteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PalpiteResponseDTO> alterarPalpite(
            @PathVariable Long palpiteId,
            @RequestBody PalpiteAlteracaoRequestDTO request) {

        PalpiteResponseDTO palpiteAlterado = palpiteService.alterarPalpite(palpiteId, request);
        return ResponseEntity.ok(palpiteAlterado);
    }

    @PutMapping("/enviar/{participanteBolaoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> enviarPalpites(@PathVariable Long participanteBolaoId) {
        return ResponseEntity.ok(palpiteService.enviarPalpites(participanteBolaoId));
    }

    @PutMapping("/liberar-alteracao")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> liberarAlteracaoPeloAdmin(
            @RequestBody LiberarAlteracaoPalpiteRequestDTO request) {

        return ResponseEntity.ok(palpiteService.liberarAlteracaoPeloAdmin(request));
    }

    @GetMapping("/jogo/{jogoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PalpiteResponseDTO>> listarPorJogo(@PathVariable Long jogoId) {
        return ResponseEntity.ok(palpiteService.listarPorJogo(jogoId));
    }

    @GetMapping("/participante/{participanteBolaoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<PalpiteResponseDTO>> listarPorParticipante(
            @PathVariable Long participanteBolaoId) {

        return ResponseEntity.ok(palpiteService.listarPorParticipante(participanteBolaoId));
    }

    @GetMapping("/bolao/{bolaoId}/participante/{participanteBolaoId}/enviados")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<PalpiteResponseDTO>> listarPalpitesEnviadosPorBolao(
            @PathVariable Long bolaoId,
            @PathVariable Long participanteBolaoId) {

        return ResponseEntity.ok(
                palpiteService.listarPalpitesEnviadosPorBolao(bolaoId, participanteBolaoId)
        );
    }
}