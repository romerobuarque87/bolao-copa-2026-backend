package com.bolao.copa2026.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.dto.ClassificacaoGrupoDTO;
import com.bolao.copa2026.service.ClassificacaoService;

@RestController
@RequestMapping("/classificacao")
public class ClassificacaoController {

    private final ClassificacaoService classificacaoService;

    public ClassificacaoController(ClassificacaoService classificacaoService) {
        this.classificacaoService = classificacaoService;
    }

    @GetMapping("/grupos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Map<String, List<ClassificacaoGrupoDTO>>> listarTodosGrupos() {
        return ResponseEntity.ok(classificacaoService.listarClassificacaoTodosGrupos());
    }

    @GetMapping("/grupo/{grupo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ClassificacaoGrupoDTO>> listarGrupo(@PathVariable String grupo) {
        return ResponseEntity.ok(classificacaoService.calcularClassificacaoGrupo(grupo));
    }
}