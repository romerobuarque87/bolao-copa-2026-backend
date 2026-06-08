package com.bolao.copa2026.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.dto.LoginRequestDTO;
import com.bolao.copa2026.dto.LoginResponseDTO;
import com.bolao.copa2026.dto.UsuarioLogadoResponseDTO;
import com.bolao.copa2026.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO request) {

        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioLogadoResponseDTO> me(
            Authentication authentication) {

        UsuarioLogadoResponseDTO response =
                authService.buscarUsuarioLogado(authentication);

        return ResponseEntity.ok(response);
    }
}