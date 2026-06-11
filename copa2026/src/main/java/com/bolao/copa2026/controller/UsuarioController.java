package com.bolao.copa2026.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.dto.AlterarSenhaRequestDTO;
import com.bolao.copa2026.dto.AtualizarUsuarioLogadoRequestDTO;
import com.bolao.copa2026.dto.ResetarSenhaUsuarioRequestDTO;
import com.bolao.copa2026.dto.UsuarioRequestDTO;
import com.bolao.copa2026.dto.UsuarioResponseDTO;
import com.bolao.copa2026.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public UsuarioResponseDTO cadastrar(@RequestBody UsuarioRequestDTO dto) {
        return usuarioService.cadastrar(dto);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UsuarioResponseDTO atualizarUsuarioLogado(
            Authentication authentication,
            @RequestBody AtualizarUsuarioLogadoRequestDTO dto) {

        return usuarioService.atualizarUsuarioLogado(authentication, dto);
    }

    @PutMapping("/me/senha")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String alterarSenha(
            Authentication authentication,
            @RequestBody @Valid AlterarSenhaRequestDTO dto) {

        return usuarioService.alterarSenha(authentication, dto);
    }
    @PutMapping("/{id}/resetar-senha")
    @PreAuthorize("hasRole('ADMIN')")
    public String resetarSenhaPorAdmin(
        @PathVariable Long id,
        @RequestBody ResetarSenhaUsuarioRequestDTO dto) {

        return usuarioService.resetarSenhaPorAdmin(id, dto);
}

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/{id}/desativar")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO desativar(@PathVariable Long id) {
        return usuarioService.desativar(id);
    }

    @PutMapping("/{id}/ativar")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO ativar(@PathVariable Long id) {
        return usuarioService.ativar(id);
    }
}