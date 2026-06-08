package com.bolao.copa2026.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.dto.RankingResponseDTO;
import com.bolao.copa2026.service.RankingService;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/bolao/{bolaoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<RankingResponseDTO>> buscarRankingPorBolao(
            @PathVariable Long bolaoId) {

        return ResponseEntity.ok(
                rankingService.buscarRankingPorBolao(bolaoId)
        );
    }
}