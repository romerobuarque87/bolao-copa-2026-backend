package com.bolao.copa2026.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.dto.BolaoRequestDTO;
import com.bolao.copa2026.dto.BolaoResponseDTO;
import com.bolao.copa2026.service.BolaoService;

@RestController
@RequestMapping("/boloes")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class BolaoController {

    private final BolaoService bolaoService;

    public BolaoController(BolaoService bolaoService) {
        this.bolaoService = bolaoService;
    }

    @PostMapping
    public BolaoResponseDTO criar(@RequestBody BolaoRequestDTO dto) {
        return bolaoService.criar(dto);
    }

    @GetMapping
    public List<BolaoResponseDTO> listar() {
        return bolaoService.listar();
    }

    @GetMapping("/{id}")
    public BolaoResponseDTO buscarPorId(@PathVariable Long id) {
        return bolaoService.buscarPorId(id);
    }

    @GetMapping("/codigo/{codigoConvite}")
    public BolaoResponseDTO buscarPorCodigoConvite(@PathVariable String codigoConvite) {
        return bolaoService.buscarPorCodigoConvite(codigoConvite);
    }
}