package com.bolao.copa2026.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.dto.LiberarAlteracaoPalpiteRequestDTO;
import com.bolao.copa2026.dto.PalpiteAlteracaoRequestDTO;
import com.bolao.copa2026.dto.PalpiteRequestDTO;
import com.bolao.copa2026.dto.PalpiteResponseDTO;
import com.bolao.copa2026.exception.BusinessException;
import com.bolao.copa2026.exception.ResourceNotFoundException;
import com.bolao.copa2026.model.Jogo;
import com.bolao.copa2026.model.Palpite;
import com.bolao.copa2026.model.ParticipanteBolao;
import com.bolao.copa2026.model.Usuario;
import com.bolao.copa2026.repository.JogoRepository;
import com.bolao.copa2026.repository.PalpiteRepository;
import com.bolao.copa2026.repository.ParticipanteBolaoRepository;
import com.bolao.copa2026.repository.UsuarioRepository;

@Service
public class PalpiteService {

    private static final LocalDateTime DATA_LIMITE_PALPITES =
            LocalDateTime.of(2026, 6, 11, 23, 59, 59);

    private final PalpiteRepository palpiteRepository;
    private final ParticipanteBolaoRepository participanteBolaoRepository;
    private final JogoRepository jogoRepository;
    private final UsuarioRepository usuarioRepository;

    public PalpiteService(
            PalpiteRepository palpiteRepository,
            ParticipanteBolaoRepository participanteBolaoRepository,
            JogoRepository jogoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.palpiteRepository = palpiteRepository;
        this.participanteBolaoRepository = participanteBolaoRepository;
        this.jogoRepository = jogoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public PalpiteResponseDTO criar(PalpiteRequestDTO request) {
        validarDataLimite();

        ParticipanteBolao participanteBolao = participanteBolaoRepository
                .findById(request.getParticipanteBolaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Participante do bolão não encontrado"));

        if (Boolean.TRUE.equals(participanteBolao.getPalpitesEnviados())) {
            throw new BusinessException("Os palpites deste participante já foram enviados e não podem mais ser alterados");
        }

        Jogo jogo = jogoRepository.findById(request.getJogoId())
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado"));

        if (Boolean.TRUE.equals(jogo.getFinalizado())) {
            throw new BusinessException("Não é possível palpitar em jogo já finalizado");
        }

        boolean jaExistePalpite = palpiteRepository.existsByParticipanteBolaoAndJogo(
                participanteBolao,
                jogo
        );

        if (jaExistePalpite) {
            throw new BusinessException("Este participante já fez palpite para este jogo");
        }

        Palpite palpite = new Palpite();
        palpite.setParticipanteBolao(participanteBolao);
        palpite.setJogo(jogo);
        palpite.setGolsCasaPalpite(request.getGolsCasaPalpite());
        palpite.setGolsVisitantePalpite(request.getGolsVisitantePalpite());
        palpite.setPontosObtidos(0);
        palpite.setAlteracaoLiberadaPeloAdmin(false);

        Palpite palpiteSalvo = palpiteRepository.save(palpite);

        return toResponseDTO(palpiteSalvo);
    }

    public PalpiteResponseDTO alterarPalpite(Long palpiteId, PalpiteAlteracaoRequestDTO request) {
        Palpite palpite = palpiteRepository.findById(palpiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Palpite não encontrado"));

        ParticipanteBolao participanteBolao = palpite.getParticipanteBolao();

        boolean participanteAindaNaoEnviou =
                !Boolean.TRUE.equals(participanteBolao.getPalpitesEnviados());

        boolean palpiteLiberadoPeloAdmin =
                Boolean.TRUE.equals(palpite.getAlteracaoLiberadaPeloAdmin());

        if (!participanteAindaNaoEnviou && !palpiteLiberadoPeloAdmin) {
            throw new BusinessException("Este palpite está bloqueado. Solicite liberação ao administrador");
        }

        if (Boolean.TRUE.equals(palpite.getJogo().getFinalizado())) {
            throw new BusinessException("Não é possível alterar palpite de jogo já finalizado");
        }

        palpite.setGolsCasaPalpite(request.getGolsCasaPalpite());
        palpite.setGolsVisitantePalpite(request.getGolsVisitantePalpite());
        palpite.setPontosObtidos(0);

        palpite.setAlteracaoLiberadaPeloAdmin(false);

        Palpite palpiteSalvo = palpiteRepository.save(palpite);

        if (Boolean.TRUE.equals(participanteBolao.getPalpitesEnviados())) {
            participanteBolao.setPalpitesEnviados(false);
            participanteBolaoRepository.save(participanteBolao);
        }

        return toResponseDTO(palpiteSalvo);
    }

    public String enviarPalpites(Long participanteBolaoId) {
        validarDataLimite();

        ParticipanteBolao participanteBolao = participanteBolaoRepository
                .findById(participanteBolaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Participante do bolão não encontrado"));

        if (Boolean.TRUE.equals(participanteBolao.getPalpitesEnviados())) {
            throw new BusinessException("Os palpites já foram enviados anteriormente");
        }

        long totalJogos = jogoRepository.countByFinalizadoFalse();

        if (totalJogos == 0) {
            throw new BusinessException("Ainda não existem jogos cadastrados no sistema");
        }

        long totalPalpitesDoParticipante =
                palpiteRepository.countByParticipanteBolaoAndJogo_FinalizadoFalse(participanteBolao);

        if (totalPalpitesDoParticipante < totalJogos) {
            throw new BusinessException(
                    "Você ainda precisa palpitar em todos os jogos antes de enviar. " +
                    "Total de jogos: " + totalJogos +
                    ". Palpites feitos: " + totalPalpitesDoParticipante +
                    ". Faltam: " + (totalJogos - totalPalpitesDoParticipante)
            );
        }

        participanteBolao.setPalpitesEnviados(true);
        participanteBolao.setAlteracaoLiberadaPeloAdmin(false);
        participanteBolaoRepository.save(participanteBolao);

        return "Palpites enviados com sucesso. Agora eles estão bloqueados.";
    }

    public String liberarAlteracaoPeloAdmin(LiberarAlteracaoPalpiteRequestDTO request) {
        Usuario administrador = usuarioRepository.findById(request.getAdministradorId())
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado"));

        if (!Boolean.TRUE.equals(administrador.getAdministrador())) {
            throw new BusinessException("Usuário informado não tem permissão de administrador");
        }

        Palpite palpite = palpiteRepository.findById(request.getPalpiteId())
                .orElseThrow(() -> new ResourceNotFoundException("Palpite não encontrado"));

        if (Boolean.TRUE.equals(palpite.getJogo().getFinalizado())) {
            throw new BusinessException("Não é possível liberar alteração de palpite de jogo já finalizado");
        }

        if (request.getMotivo() == null || request.getMotivo().isBlank()) {
            throw new BusinessException("Informe o motivo da liberação da alteração");
        }

        palpite.setAlteracaoLiberadaPeloAdmin(true);
        palpite.setAdministradorQueLiberou(administrador);
        palpite.setDataLiberacaoAlteracao(LocalDateTime.now());
        palpite.setMotivoLiberacaoAlteracao(request.getMotivo());

        palpiteRepository.save(palpite);

        return "Alteração liberada pelo administrador apenas para o palpite informado.";
    }

    public List<PalpiteResponseDTO> listarPorJogo(Long jogoId) {
        Jogo jogo = jogoRepository.findById(jogoId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado"));

        return palpiteRepository.findByJogo(jogo)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<PalpiteResponseDTO> listarPorParticipante(Long participanteBolaoId) {
        ParticipanteBolao participanteBolao = participanteBolaoRepository
                .findById(participanteBolaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Participante do bolão não encontrado"));

        return palpiteRepository.findByParticipanteBolao(participanteBolao)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private void validarDataLimite() {
        if (LocalDateTime.now().isAfter(DATA_LIMITE_PALPITES)) {
            throw new BusinessException("O prazo para enviar palpites já foi encerrado");
        }
    }

    private PalpiteResponseDTO toResponseDTO(Palpite palpite) {
        PalpiteResponseDTO response = new PalpiteResponseDTO();

        response.setId(palpite.getId());

        response.setParticipanteBolaoId(palpite.getParticipanteBolao().getId());
        response.setNomeUsuario(palpite.getParticipanteBolao().getUsuario().getNome());
        response.setBolaoId(palpite.getParticipanteBolao().getBolao().getId());
        response.setNomeBolao(palpite.getParticipanteBolao().getBolao().getNome());

        response.setJogoId(palpite.getJogo().getId());
        response.setTimeCasaNome(palpite.getJogo().getTimeCasa().getNome());
        response.setTimeCasaSigla(palpite.getJogo().getTimeCasa().getSiglaFifa());
        response.setTimeVisitanteNome(palpite.getJogo().getTimeVisitante().getNome());
        response.setTimeVisitanteSigla(palpite.getJogo().getTimeVisitante().getSiglaFifa());
        response.setDataHoraJogo(palpite.getJogo().getDataHora());
        response.setFase(palpite.getJogo().getFase());

        response.setGolsCasaPalpite(palpite.getGolsCasaPalpite());
        response.setGolsVisitantePalpite(palpite.getGolsVisitantePalpite());
        response.setPontosObtidos(palpite.getPontosObtidos());

        return response;
    }
}