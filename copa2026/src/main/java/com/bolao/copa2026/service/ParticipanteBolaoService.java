package com.bolao.copa2026.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.dto.EntrarBolaoRequestDTO;
import com.bolao.copa2026.dto.ParticipanteBolaoResponseDTO;
import com.bolao.copa2026.model.Bolao;
import com.bolao.copa2026.model.ParticipanteBolao;
import com.bolao.copa2026.model.Usuario;
import com.bolao.copa2026.repository.BolaoRepository;
import com.bolao.copa2026.repository.ParticipanteBolaoRepository;
import com.bolao.copa2026.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipanteBolaoService {

    private final ParticipanteBolaoRepository participanteBolaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BolaoRepository bolaoRepository;

    public ParticipanteBolaoResponseDTO entrarNoBolao(EntrarBolaoRequestDTO request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Bolao bolao = bolaoRepository.findByCodigoConvite(request.getCodigoConvite())
                .orElseThrow(() -> new RuntimeException("Código de convite inválido"));

        boolean jaParticipa = participanteBolaoRepository.existsByUsuarioAndBolao(usuario, bolao);

        if (jaParticipa) {
            throw new RuntimeException("Usuário já participa deste bolão");
        }

        ParticipanteBolao participante = new ParticipanteBolao();
        participante.setUsuario(usuario);
        participante.setBolao(bolao);

        ParticipanteBolao participanteSalvo = participanteBolaoRepository.save(participante);

        return toResponseDTO(participanteSalvo);
    }

    public List<ParticipanteBolaoResponseDTO> listarPorBolao(Long bolaoId) {
        return participanteBolaoRepository.findByBolaoId(bolaoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ParticipanteBolaoResponseDTO> listarPorUsuario(Long usuarioId) {
        return participanteBolaoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

   private ParticipanteBolaoResponseDTO toResponseDTO(ParticipanteBolao participante) {

    ParticipanteBolaoResponseDTO response = new ParticipanteBolaoResponseDTO();

    response.setId(participante.getId());
    response.setUsuarioId(participante.getUsuario().getId());
    response.setNomeUsuario(participante.getUsuario().getNome());
    response.setBolaoId(participante.getBolao().getId());
    response.setNomeBolao(participante.getBolao().getNome());
    response.setCodigoConvite(participante.getBolao().getCodigoConvite());
    response.setPontos(participante.getPontos());
    response.setPalpitesEnviados(participante.getPalpitesEnviados());

    return response;
}
}