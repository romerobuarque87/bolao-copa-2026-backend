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

import com.bolao.copa2026.dto.EntrarBolaoRequestDTO;
import com.bolao.copa2026.dto.ParticipanteBolaoResponseDTO;
import com.bolao.copa2026.service.ParticipanteBolaoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boloes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ParticipanteBolaoController {

    private final ParticipanteBolaoService participanteBolaoService;

    @PostMapping("/entrar")
    public ResponseEntity<ParticipanteBolaoResponseDTO> entrarNoBolao(
            @RequestBody EntrarBolaoRequestDTO request) {

        ParticipanteBolaoResponseDTO response =
                participanteBolaoService.entrarNoBolao(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{bolaoId}/participantes")
    public ResponseEntity<List<ParticipanteBolaoResponseDTO>>
            listarParticipantesDoBolao(
                    @PathVariable Long bolaoId) {

        List<ParticipanteBolaoResponseDTO> participantes =
                participanteBolaoService.listarPorBolao(bolaoId);

        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ParticipanteBolaoResponseDTO>>
            listarBoloesDoUsuario(
                    @PathVariable Long usuarioId) {

        List<ParticipanteBolaoResponseDTO> boloes =
                participanteBolaoService.listarPorUsuario(usuarioId);

        return ResponseEntity.ok(boloes);
    }
}