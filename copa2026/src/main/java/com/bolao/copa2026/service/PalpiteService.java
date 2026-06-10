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
import com.bolao.copa2026.model.ConfiguracaoPontuacao;
import com.bolao.copa2026.model.FaseCopa;
import com.bolao.copa2026.model.Jogo;
import com.bolao.copa2026.model.Palpite;
import com.bolao.copa2026.model.ParticipanteBolao;
import com.bolao.copa2026.model.Selecao;
import com.bolao.copa2026.model.Usuario;
import com.bolao.copa2026.repository.JogoRepository;
import com.bolao.copa2026.repository.PalpiteRepository;
import com.bolao.copa2026.repository.ParticipanteBolaoRepository;
import com.bolao.copa2026.repository.SelecaoRepository;
import com.bolao.copa2026.repository.UsuarioRepository;

@Service
public class PalpiteService {

    private static final LocalDateTime DATA_LIMITE_PALPITES =
            LocalDateTime.of(2026, 6, 11, 23, 59, 59);

    private final PalpiteRepository palpiteRepository;
    private final ParticipanteBolaoRepository participanteBolaoRepository;
    private final JogoRepository jogoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SelecaoRepository selecaoRepository;
    private final ConfiguracaoPontuacaoService configuracaoPontuacaoService;

    public PalpiteService(
            PalpiteRepository palpiteRepository,
            ParticipanteBolaoRepository participanteBolaoRepository,
            JogoRepository jogoRepository,
            UsuarioRepository usuarioRepository,
            SelecaoRepository selecaoRepository,
            ConfiguracaoPontuacaoService configuracaoPontuacaoService
    ) {
        this.palpiteRepository = palpiteRepository;
        this.participanteBolaoRepository = participanteBolaoRepository;
        this.jogoRepository = jogoRepository;
        this.usuarioRepository = usuarioRepository;
        this.selecaoRepository = selecaoRepository;
        this.configuracaoPontuacaoService = configuracaoPontuacaoService;
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

        Selecao classificadoPalpite = buscarClassificadoPalpiteSeNecessario(jogo, request.getClassificadoPalpiteId());

        Palpite palpite = new Palpite();
        palpite.setParticipanteBolao(participanteBolao);
        palpite.setJogo(jogo);
        palpite.setGolsCasaPalpite(request.getGolsCasaPalpite());
        palpite.setGolsVisitantePalpite(request.getGolsVisitantePalpite());
        palpite.setClassificadoPalpite(classificadoPalpite);
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

    private Selecao buscarClassificadoPalpiteSeNecessario(Jogo jogo, Long classificadoPalpiteId) {
        if (jogo.getFase() == FaseCopa.GRUPOS) {
            return null;
        }

        if (classificadoPalpiteId == null) {
            throw new BusinessException("Em jogos de mata-mata, informe quem você acha que vai se classificar");
        }

        Selecao classificado = selecaoRepository.findById(classificadoPalpiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Seleção classificada do palpite não encontrada"));

        boolean ehTimeDoJogo =
                classificado.getId().equals(jogo.getTimeCasa().getId()) ||
                classificado.getId().equals(jogo.getTimeVisitante().getId());

        if (!ehTimeDoJogo) {
            throw new BusinessException("O classificado informado precisa ser um dos times do jogo");
        }

        return classificado;
    }

    private Selecao descobrirVencedorReal(Jogo jogo) {
        if (jogo.getGolsCasa() == null || jogo.getGolsVisitante() == null) {
            return null;
        }

        if (jogo.getGolsCasa() > jogo.getGolsVisitante()) {
            return jogo.getTimeCasa();
        }

        if (jogo.getGolsVisitante() > jogo.getGolsCasa()) {
            return jogo.getTimeVisitante();
        }

        if (jogo.getPenaltisCasa() == null || jogo.getPenaltisVisitante() == null) {
            return null;
        }

        return jogo.getPenaltisCasa() > jogo.getPenaltisVisitante()
                ? jogo.getTimeCasa()
                : jogo.getTimeVisitante();
    }

    public int calcularPontosDoPalpite(Jogo jogo, Palpite palpite) {
        ConfiguracaoPontuacao config = configuracaoPontuacaoService.buscarConfiguracao();

        boolean placarExato =
                jogo.getGolsCasa().equals(palpite.getGolsCasaPalpite()) &&
                jogo.getGolsVisitante().equals(palpite.getGolsVisitantePalpite());

        if (placarExato && jogo.getFase() == FaseCopa.GRUPOS) {
            return config.getPontosPlacarExato();
        }

        int pontos = 0;

        if (placarExato) {
            pontos += config.getPontosPlacarExato();
        }

        int resultadoReal = Integer.compare(jogo.getGolsCasa(), jogo.getGolsVisitante());

        int resultadoPalpite = Integer.compare(
                palpite.getGolsCasaPalpite(),
                palpite.getGolsVisitantePalpite()
        );

        if (resultadoReal == resultadoPalpite) {
            pontos += config.getPontosResultado();
        }

        if (jogo.getGolsCasa().equals(palpite.getGolsCasaPalpite())) {
            pontos += config.getPontosGolsMandante();
        }

        if (jogo.getGolsVisitante().equals(palpite.getGolsVisitantePalpite())) {
            pontos += config.getPontosGolsVisitante();
        }

        if (jogo.getFase() != FaseCopa.GRUPOS && palpite.getClassificadoPalpite() != null) {
            Selecao vencedorReal = descobrirVencedorReal(jogo);

            if (vencedorReal != null &&
                    vencedorReal.getId().equals(palpite.getClassificadoPalpite().getId())) {
                pontos += config.getPontosClassificado();
            }
        }

        return pontos;
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
        response.setFase(palpite.getJogo().getFase().name());

        response.setGolsCasaPalpite(palpite.getGolsCasaPalpite());
        response.setGolsVisitantePalpite(palpite.getGolsVisitantePalpite());

        if (palpite.getClassificadoPalpite() != null) {
            response.setClassificadoPalpiteId(palpite.getClassificadoPalpite().getId());
            response.setClassificadoPalpiteNome(palpite.getClassificadoPalpite().getNome());
            response.setClassificadoPalpiteSigla(palpite.getClassificadoPalpite().getSiglaFifa());
        }

        response.setPontosObtidos(palpite.getPontosObtidos());

        return response;
    }
}