package com.bolao.copa2026.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.dto.JogoRequestDTO;
import com.bolao.copa2026.dto.JogoResponseDTO;
import com.bolao.copa2026.exception.BusinessException;
import com.bolao.copa2026.exception.ResourceNotFoundException;
import com.bolao.copa2026.model.ConfiguracaoPontuacao;
import com.bolao.copa2026.model.Estadio;
import com.bolao.copa2026.model.Jogo;
import com.bolao.copa2026.model.Palpite;
import com.bolao.copa2026.model.ParticipanteBolao;
import com.bolao.copa2026.model.Selecao;
import com.bolao.copa2026.repository.EstadioRepository;
import com.bolao.copa2026.repository.JogoRepository;
import com.bolao.copa2026.repository.PalpiteRepository;
import com.bolao.copa2026.repository.ParticipanteBolaoRepository;
import com.bolao.copa2026.repository.SelecaoRepository;

import jakarta.transaction.Transactional;

@Service
public class JogoService {

    private final JogoRepository jogoRepository;
    private final PalpiteRepository palpiteRepository;
    private final ParticipanteBolaoRepository participanteBolaoRepository;
    private final ConfiguracaoPontuacaoService configuracaoPontuacaoService;
    private final SelecaoRepository selecaoRepository;
    private final EstadioRepository estadioRepository;

    public JogoService(
            JogoRepository jogoRepository,
            PalpiteRepository palpiteRepository,
            ParticipanteBolaoRepository participanteBolaoRepository,
            ConfiguracaoPontuacaoService configuracaoPontuacaoService,
            SelecaoRepository selecaoRepository,
            EstadioRepository estadioRepository) {
        this.jogoRepository = jogoRepository;
        this.palpiteRepository = palpiteRepository;
        this.participanteBolaoRepository = participanteBolaoRepository;
        this.configuracaoPontuacaoService = configuracaoPontuacaoService;
        this.selecaoRepository = selecaoRepository;
        this.estadioRepository = estadioRepository;
    }

    public JogoResponseDTO criar(JogoRequestDTO request) {
        Selecao timeCasa = selecaoRepository.findById(request.getTimeCasaId())
                .orElseThrow(() -> new ResourceNotFoundException("Seleção mandante não encontrada"));

        Selecao timeVisitante = selecaoRepository.findById(request.getTimeVisitanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Seleção visitante não encontrada"));

        Estadio estadio = estadioRepository.findById(request.getEstadioId())
                .orElseThrow(() -> new ResourceNotFoundException("Estádio não encontrado"));

        Jogo jogo = new Jogo();
        jogo.setTimeCasa(timeCasa);
        jogo.setTimeVisitante(timeVisitante);
        jogo.setEstadio(estadio);
        jogo.setDataHora(request.getDataHora());
        jogo.setFase(request.getFase());
        jogo.setFinalizado(false);
        jogo.setGolsCasa(null);
        jogo.setGolsVisitante(null);

        Jogo jogoSalvo = jogoRepository.save(jogo);

        return toResponseDTO(jogoSalvo);
    }

    public List<JogoResponseDTO> listarTodos() {
        return jogoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public JogoResponseDTO buscarPorId(Long id) {
        Jogo jogo = jogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado"));

        return toResponseDTO(jogo);
    }

    @Transactional
    public JogoResponseDTO finalizarJogo(Long jogoId, Integer golsCasa, Integer golsVisitante) {
        Jogo jogo = jogoRepository.findById(jogoId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado"));

        if (Boolean.TRUE.equals(jogo.getFinalizado())) {
            throw new BusinessException("Este jogo já foi finalizado");
        }

        jogo.setGolsCasa(golsCasa);
        jogo.setGolsVisitante(golsVisitante);
        jogo.setFinalizado(true);

        Jogo jogoSalvo = jogoRepository.save(jogo);

        calcularPontuacaoDosPalpites(jogoSalvo);

        return toResponseDTO(jogoSalvo);
    }

    @Transactional
    public JogoResponseDTO corrigirResultado(Long jogoId, Integer golsCasa, Integer golsVisitante) {
        Jogo jogo = jogoRepository.findById(jogoId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado"));

        if (!Boolean.TRUE.equals(jogo.getFinalizado())) {
            throw new BusinessException("Só é possível corrigir um jogo já finalizado");
        }

        estornarPontuacaoDosPalpites(jogo);

        jogo.setGolsCasa(golsCasa);
        jogo.setGolsVisitante(golsVisitante);
        jogo.setFinalizado(true);

        Jogo jogoSalvo = jogoRepository.save(jogo);

        calcularPontuacaoDosPalpites(jogoSalvo);

        return toResponseDTO(jogoSalvo);
    }

    private void calcularPontuacaoDosPalpites(Jogo jogo) {
        List<Palpite> palpites = palpiteRepository.findByJogo(jogo);

        for (Palpite palpite : palpites) {
            int pontos = calcularPontos(jogo, palpite);

            palpite.setPontosObtidos(pontos);
            palpiteRepository.save(palpite);

            ParticipanteBolao participanteBolao = palpite.getParticipanteBolao();
            participanteBolao.setPontos(participanteBolao.getPontos() + pontos);
            participanteBolaoRepository.save(participanteBolao);
        }
    }

    private void estornarPontuacaoDosPalpites(Jogo jogo) {
        List<Palpite> palpites = palpiteRepository.findByJogo(jogo);

        for (Palpite palpite : palpites) {
            Integer pontosAntigos = palpite.getPontosObtidos();

            if (pontosAntigos == null) {
                pontosAntigos = 0;
            }

            ParticipanteBolao participanteBolao = palpite.getParticipanteBolao();
            participanteBolao.setPontos(participanteBolao.getPontos() - pontosAntigos);
            participanteBolaoRepository.save(participanteBolao);

            palpite.setPontosObtidos(0);
            palpiteRepository.save(palpite);
        }
    }

    private int calcularPontos(Jogo jogo, Palpite palpite) {
        ConfiguracaoPontuacao config = configuracaoPontuacaoService.buscarConfiguracao();

        boolean placarExato =
                jogo.getGolsCasa().equals(palpite.getGolsCasaPalpite()) &&
                jogo.getGolsVisitante().equals(palpite.getGolsVisitantePalpite());

        if (placarExato) {
            return config.getPontosPlacarExato();
        }

        int pontos = 0;

        if (acertouResultado(jogo, palpite)) {
            pontos += config.getPontosResultado();
        }

        if (jogo.getGolsCasa().equals(palpite.getGolsCasaPalpite())) {
            pontos += config.getPontosGolsMandante();
        }

        if (jogo.getGolsVisitante().equals(palpite.getGolsVisitantePalpite())) {
            pontos += config.getPontosGolsVisitante();
        }

        return pontos;
    }

    private boolean acertouResultado(Jogo jogo, Palpite palpite) {
        int resultadoReal = Integer.compare(jogo.getGolsCasa(), jogo.getGolsVisitante());

        int resultadoPalpite = Integer.compare(
                palpite.getGolsCasaPalpite(),
                palpite.getGolsVisitantePalpite()
        );

        return resultadoReal == resultadoPalpite;
    }

    private JogoResponseDTO toResponseDTO(Jogo jogo) {
        JogoResponseDTO response = new JogoResponseDTO();

        response.setId(jogo.getId());

        response.setTimeCasaId(jogo.getTimeCasa().getId());
        response.setTimeCasaNome(jogo.getTimeCasa().getNome());
        response.setTimeCasaSigla(jogo.getTimeCasa().getSiglaFifa());

        response.setTimeVisitanteId(jogo.getTimeVisitante().getId());
        response.setTimeVisitanteNome(jogo.getTimeVisitante().getNome());
        response.setTimeVisitanteSigla(jogo.getTimeVisitante().getSiglaFifa());

        response.setEstadioId(jogo.getEstadio().getId());
        response.setEstadioNome(jogo.getEstadio().getNome());
        response.setEstadioCidade(jogo.getEstadio().getCidade());
        response.setEstadioPais(jogo.getEstadio().getPais());

        response.setGolsCasa(jogo.getGolsCasa());
        response.setGolsVisitante(jogo.getGolsVisitante());

        response.setDataHora(jogo.getDataHora());
        response.setFase(jogo.getFase());
        response.setFinalizado(jogo.getFinalizado());

        return response;
    }
}