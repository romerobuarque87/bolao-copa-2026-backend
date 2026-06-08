package com.bolao.copa2026.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bolao.copa2026.dto.LoginRequestDTO;
import com.bolao.copa2026.dto.LoginResponseDTO;
import com.bolao.copa2026.dto.UsuarioLogadoResponseDTO;
import com.bolao.copa2026.exception.BusinessException;
import com.bolao.copa2026.model.Usuario;
import com.bolao.copa2026.repository.UsuarioRepository;
import com.bolao.copa2026.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos"));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new BusinessException("Usuário desativado");
        }

        boolean senhaCorreta = passwordEncoder.matches(
                request.getSenha(),
                usuario.getSenha()
        );

        if (!senhaCorreta) {
            throw new BusinessException("Email ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAdministrador()
        );
    }

    public UsuarioLogadoResponseDTO buscarUsuarioLogado(Authentication authentication) {

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new BusinessException("Usuário não autenticado");
        }

        Usuario usuario = (Usuario) authentication.getPrincipal();

        return new UsuarioLogadoResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAdministrador()
        );
    }
}