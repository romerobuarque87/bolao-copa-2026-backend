package com.bolao.copa2026.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.dto.BolaoRequestDTO;
import com.bolao.copa2026.dto.BolaoResponseDTO;
import com.bolao.copa2026.exception.ResourceNotFoundException;
import com.bolao.copa2026.model.Bolao;
import com.bolao.copa2026.model.ParticipanteBolao;
import com.bolao.copa2026.model.Usuario;
import com.bolao.copa2026.repository.BolaoRepository;
import com.bolao.copa2026.repository.ParticipanteBolaoRepository;
import com.bolao.copa2026.repository.UsuarioRepository;

@Service
public class BolaoService {

    private final BolaoRepository bolaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParticipanteBolaoRepository participanteBolaoRepository;

    public BolaoService(
            BolaoRepository bolaoRepository,
            UsuarioRepository usuarioRepository,
            ParticipanteBolaoRepository participanteBolaoRepository) {
        this.bolaoRepository = bolaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.participanteBolaoRepository = participanteBolaoRepository;
    }

    public BolaoResponseDTO criar(BolaoRequestDTO dto) {
        Usuario organizador = usuarioRepository.findById(dto.getOrganizadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Organizador não encontrado"));

        Bolao bolao = new Bolao();
        bolao.setNome(dto.getNome());
        bolao.setOrganizador(organizador);

        Bolao bolaoSalvo = bolaoRepository.save(bolao);

        ParticipanteBolao participante = new ParticipanteBolao();
        participante.setUsuario(organizador);
        participante.setBolao(bolaoSalvo);
        participante.setPontos(0);
        participante.setPalpitesEnviados(false);

        participanteBolaoRepository.save(participante);

        return converterParaResponseDTO(bolaoSalvo);
    }

    public List<BolaoResponseDTO> listar() {
        return bolaoRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public BolaoResponseDTO buscarPorId(Long id) {
        Bolao bolao = bolaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bolão não encontrado"));

        return converterParaResponseDTO(bolao);
    }

    public BolaoResponseDTO buscarPorCodigoConvite(String codigoConvite) {
        Bolao bolao = bolaoRepository.findByCodigoConvite(codigoConvite)
                .orElseThrow(() -> new ResourceNotFoundException("Bolão não encontrado para este código de convite"));

        return converterParaResponseDTO(bolao);
    }

    private BolaoResponseDTO converterParaResponseDTO(Bolao bolao) {
        return new BolaoResponseDTO(
                bolao.getId(),
                bolao.getNome(),
                bolao.getCodigoConvite(),
                bolao.getAtivo(),
                bolao.getDataCriacao(),
                bolao.getOrganizador().getId(),
                bolao.getOrganizador().getNome()
        );
    }
}