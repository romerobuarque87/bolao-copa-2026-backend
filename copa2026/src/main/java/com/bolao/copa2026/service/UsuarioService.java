package com.bolao.copa2026.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bolao.copa2026.dto.AlterarSenhaRequestDTO;
import com.bolao.copa2026.dto.AtualizarUsuarioLogadoRequestDTO;
import com.bolao.copa2026.dto.ResetarSenhaUsuarioRequestDTO;
import com.bolao.copa2026.dto.UsuarioRequestDTO;
import com.bolao.copa2026.dto.UsuarioResponseDTO;
import com.bolao.copa2026.exception.BusinessException;
import com.bolao.copa2026.exception.ResourceNotFoundException;
import com.bolao.copa2026.model.Usuario;
import com.bolao.copa2026.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Já existe um usuário cadastrado com este e-mail");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setTelefone(dto.getTelefone());
        usuario.setReceberNotificacaoEmail(dto.getReceberNotificacaoEmail());
        usuario.setReceberNotificacaoWhatsapp(dto.getReceberNotificacaoWhatsapp());
        usuario.setAdministrador(false);
        usuario.setAtivo(true);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterParaResponseDTO(usuarioSalvo);
    }

    public UsuarioResponseDTO atualizarUsuarioLogado(
            Authentication authentication,
            AtualizarUsuarioLogadoRequestDTO dto) {

        Usuario usuario = buscarUsuarioAutenticado(authentication);

        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }

        if (dto.getTelefone() != null) {
            usuario.setTelefone(dto.getTelefone());
        }

        if (dto.getReceberNotificacaoEmail() != null) {
            usuario.setReceberNotificacaoEmail(dto.getReceberNotificacaoEmail());
        }

        if (dto.getReceberNotificacaoWhatsapp() != null) {
            usuario.setReceberNotificacaoWhatsapp(dto.getReceberNotificacaoWhatsapp());
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterParaResponseDTO(usuarioSalvo);
    }

    public String alterarSenha(
            Authentication authentication,
            AlterarSenhaRequestDTO dto) {

        Usuario usuario = buscarUsuarioAutenticado(authentication);

        boolean senhaAtualCorreta = passwordEncoder.matches(
                dto.getSenhaAtual(),
                usuario.getSenha()
        );

        if (!senhaAtualCorreta) {
            throw new BusinessException("Senha atual inválida");
        }

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));

        usuarioRepository.save(usuario);

        return "Senha alterada com sucesso";
    }

    public UsuarioResponseDTO desativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setAtivo(false);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterParaResponseDTO(usuarioSalvo);
    }

    public UsuarioResponseDTO ativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setAtivo(true);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterParaResponseDTO(usuarioSalvo);
    }
    public String resetarSenhaPorAdmin(Long id, ResetarSenhaUsuarioRequestDTO dto) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

    if (dto.getNovaSenha() == null || dto.getNovaSenha().isBlank()) {
        throw new BusinessException("Informe a nova senha");
    }

    if (dto.getNovaSenha().length() < 6) {
        throw new BusinessException("A nova senha deve ter pelo menos 6 caracteres");
    }

    usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));

    usuarioRepository.save(usuario);

    return "Senha redefinida com sucesso";
}

    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return converterParaResponseDTO(usuario);
    }

    private Usuario buscarUsuarioAutenticado(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new BusinessException("Usuário não autenticado");
        }

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        return usuarioRepository.findById(usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    private UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getReceberNotificacaoEmail(),
                usuario.getReceberNotificacaoWhatsapp(),
                usuario.getAdministrador(),
                usuario.getAtivo(),
                usuario.getDataCadastro()
        );
    }
}