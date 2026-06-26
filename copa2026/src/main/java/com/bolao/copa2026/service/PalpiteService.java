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

    private static final LocalDateTime DATA_LIMITE_PALPITES_GRUPOS =
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
        ParticipanteBolao participanteBolao = participanteBolaoRepository
                .findById(request.getParticipanteBolaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Participante do bolao nao encontrado"));

        Jogo jogo = jogoRepository.findById(request.getJogoId())
                .orElseThrow(() -> new ResourceNotFoundException("Jogo nao encontrado"));

        validarPrazoPalpite(jogo);

        if (Boolean.TRUE.equals(participanteBolao.getPalpitesEnviados()) && jogo.getFase() == FaseCopa.GRUPOS) {
            throw new BusinessException("Os palpites da fase de grupos ja foram enviados e nao podem mais ser alterados");
        }

        if (Boolean.TRUE.equals(jogo.getFinalizado())) {
            throw new BusinessException("Nao e possivel palpitar em jogo ja finalizado");
        }

        boolean jaExistePalpite = palpiteRepository.existsByParticipanteBolaoAndJogo(participanteBolao, jogo);

        if (jaExistePalpite) {
            throw new BusinessException("Este participante ja fez palpite para este jogo");
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

        return toResponseDTO(palpiteRepository.save(palpite));
    }

    public PalpiteResponseDTO alterarPalpite(Long palpiteId, PalpiteAlteracaoRequestDTO request) {
        Palpite palpite = palpiteRepository.findById(palpiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Palpite nao encontrado"));

        ParticipanteBolao participanteBolao = palpite.getParticipanteBolao();
        Jogo jogo = palpite.getJogo();

        boolean participanteAindaNaoEnviou = !Boolean.TRUE.equals(participanteBolao.getPalpitesEnviados());
        boolean palpiteLiberadoPeloAdmin = Boolean.TRUE.equals(palpite.getAlteracaoLiberadaPeloAdmin());

        if (jogo.getFase() == FaseCopa.GRUPOS && !participanteAindaNaoEnviou && !palpiteLiberadoPeloAdmin) {
            throw new BusinessException("Este palpite esta bloqueado. Solicite liberacao ao administrador");
        }

        validarPrazoPalpite(jogo);

        if (Boolean.TRUE.equals(jogo.getFinalizado())) {
            throw new BusinessException("Nao e possivel alterar palpite de jogo ja finalizado");
        }

        Selecao classificadoPalpite = buscarClassificadoPalpiteSeNecessario(jogo, request.getClassificadoPalpiteId());

        palpite.setGolsCasaPalpite(request.getGolsCasaPalpite());
        palpite.setGolsVisitantePalpite(request.getGolsVisitantePalpite());
        palpite.setClassificadoPalpite(classificadoPalpite);
        palpite.setPontosObtidos(0);
        palpite.setAlteracaoLiberadaPeloAdmin(false);

        Palpite palpiteSalvo = palpiteRepository.save(palpite);

        if (jogo.getFase() == FaseCopa.GRUPOS && Boolean.TRUE.equals(participanteBolao.getPalpitesEnviados())) {
            participanteBolao.setPalpitesEnviados(false);
            participanteBolaoRepository.save(participanteBolao);
        }

        return toResponseDTO(palpiteSalvo);
    }

    public String enviarPalpites(Long participanteBolaoId) {
        validarDataLimiteGrupos();

        ParticipanteBolao participanteBolao = participanteBolaoRepository
                .findById(participanteBolaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Participante do bolao nao encontrado"));

        if (Boolean.TRUE.equals(participanteBolao.getPalpitesEnviados())) {
            throw new BusinessException("Os palpites ja foram enviados anteriormente");
        }

        long totalJogos = jogoRepository.countByFinalizadoFalse();

        if (totalJogos == 0) {
            throw new BusinessException("Ainda nao existem jogos cadastrados no sistema");
        }

        long totalPalpitesDoParticipante =
                palpiteRepository.countByParticipanteBolaoAndJogo_FinalizadoFalse(participanteBolao);

        if (totalPalpitesDoParticipante < totalJogos) {
            throw new BusinessException(
                    "Voce ainda precisa palpitar em todos os jogos antes de enviar. " +
                    "Total de jogos: " + totalJogos +
                    ". Palpites feitos: " + totalPalpitesDoParticipante +
                    ". Faltam: " + (totalJogos - totalPalpitesDoParticipante)
            );
        }

        participanteBolao.setPalpitesEnviados(true);
        participanteBolao.setAlteracaoLiberadaPeloAdmin(false);
        participanteBolaoRepository.save(participanteBolao);

        return "Palpites enviados com sucesso. Agora eles estao bloqueados.";
    }

    public String liberarAlteracaoPeloAdmin(LiberarAlteracaoPalpiteRequestDTO request) {
        Usuario administrador = usuarioRepository.findById(request.getAdministradorId())
                .orElseThrow(() -> new ResourceNotFoundException("Administrador nao encontrado"));

        if (!Boolean.TRUE.equals(administrador.getAdministrador())) {
            throw new BusinessException("Usuario informado nao tem permissao de administrador");
        }

        Palpite palpite = palpiteRepository.findById(request.getPalpiteId())
                .orElseThrow(() -> new ResourceNotFoundException("Palpite nao encontrado"));

        if (Boolean.TRUE.equals(palpite.getJogo().getFinalizado())) {
            throw new BusinessException("Nao e possivel liberar alteracao de palpite de jogo ja finalizado");
        }

        if (request.getMotivo() == null || request.getMotivo().isBlank()) {
            throw new BusinessException("Informe o motivo da liberacao da alteracao");
        }

        palpite.setAlteracaoLiberadaPeloAdmin(true);
        palpite.setAdministradorQueLiberou(administrador);
        palpite.setDataLiberacaoAlteracao(LocalDateTime.now());
        palpite.setMotivoLiberacaoAlteracao(request.getMotivo());

        palpiteRepository.save(palpite);

        return "Alteracao liberada pelo administrador apenas para o palpite informado.";
    }

    public List<PalpiteResponseDTO> listarPorJogo(Long jogoId) {
        Jogo jogo = jogoRepository.findById(jogoId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo nao encontrado"));

        return palpiteRepository.findByJogo(jogo)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<PalpiteResponseDTO> listarPorParticipante(Long participanteBolaoId) {
        ParticipanteBolao participanteBolao = participanteBolaoRepository
                .findById(participanteBolaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Participante do bolao nao encontrado"));

        return palpiteRepository.findByParticipanteBolao(participanteBolao)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<PalpiteResponseDTO> listarPalpitesEnviadosPorBolao(Long bolaoId, Long participanteBolaoId) {
        ParticipanteBolao participanteBolao = participanteBolaoRepository
                .findById(participanteBolaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Participante do bolao nao encontrado"));

        if (!participanteBolao.getBolao().getId().equals(bolaoId)) {
            throw new BusinessException("Este participante nao pertence ao bolao informado");
        }

        boolean ehAdmin = Boolean.TRUE.equals(participanteBolao.getUsuario().getAdministrador());

        if (!ehAdmin && !Boolean.TRUE.equals(participanteBolao.getPalpitesEnviados())) {
            throw new BusinessException("Voce so pode ver os palpites dos outros participantes depois de enviar os seus.");
        }

        return palpiteRepository
                .findByParticipanteBolao_Bolao_IdAndParticipanteBolao_PalpitesEnviadosTrueOrderByParticipanteBolao_Usuario_NomeAscJogo_DataHoraAsc(bolaoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private Selecao buscarClassificadoPalpiteSeNecessario(Jogo jogo, Long classificadoPalpiteId) {
        if (jogo.getFase() == FaseCopa.GRUPOS) {
            return null;
        }

        if (classificadoPalpiteId == null) {
            throw new BusinessException("Em jogos de mata-mata, informe quem voce acha que vai se classificar");
        }

        Selecao classificado = selecaoRepository.findById(classificadoPalpiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Selecao classificada do palpite nao encontrada"));

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

    int pontos = 0;

    if (placarExato) {
        pontos += config.getPontosPlacarExato();
    } else {
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

    private void validarDataLimiteGrupos() {
        if (LocalDateTime.now().isAfter(DATA_LIMITE_PALPITES_GRUPOS)) {
            throw new BusinessException("O prazo para enviar palpites da fase de grupos ja foi encerrado");
        }
    }

    private void validarPrazoPalpite(Jogo jogo) {
        LocalDateTime dataLimite = jogo.getFase() == FaseCopa.GRUPOS
                ? DATA_LIMITE_PALPITES_GRUPOS
                : jogo.getDataHora();

        if (dataLimite != null && LocalDateTime.now().isAfter(dataLimite)) {
            throw new BusinessException("O prazo para palpitar neste jogo ja foi encerrado");
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
