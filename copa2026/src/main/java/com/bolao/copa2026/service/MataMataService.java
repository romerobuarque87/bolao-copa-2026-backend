package com.bolao.copa2026.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bolao.copa2026.dto.ClassificacaoGrupoDTO;
import com.bolao.copa2026.exception.BusinessException;
import com.bolao.copa2026.model.Estadio;
import com.bolao.copa2026.model.FaseCopa;
import com.bolao.copa2026.model.Jogo;
import com.bolao.copa2026.model.Selecao;
import com.bolao.copa2026.repository.EstadioRepository;
import com.bolao.copa2026.repository.JogoRepository;
import com.bolao.copa2026.repository.SelecaoRepository;

import jakarta.transaction.Transactional;

@Service
public class MataMataService {

    private final ClassificacaoService classificacaoService;
    private final JogoRepository jogoRepository;
    private final SelecaoRepository selecaoRepository;
    private final EstadioRepository estadioRepository;

    public MataMataService(
            ClassificacaoService classificacaoService,
            JogoRepository jogoRepository,
            SelecaoRepository selecaoRepository,
            EstadioRepository estadioRepository
    ) {
        this.classificacaoService = classificacaoService;
        this.jogoRepository = jogoRepository;
        this.selecaoRepository = selecaoRepository;
        this.estadioRepository = estadioRepository;
    }

    @Transactional
    public String gerarDezesseisAvos() {
        if (jogoRepository.countByFase(FaseCopa.DEZESSEIS_AVOS) > 0) {
            throw new BusinessException("Os jogos dos dezesseis-avos ja foram gerados.");
        }

        validarGruposFinalizados();

        List<ClassificacaoGrupoDTO> primeiros = new ArrayList<>();
        List<ClassificacaoGrupoDTO> segundos = new ArrayList<>();
        List<ClassificacaoGrupoDTO> terceiros = new ArrayList<>();

        String[] grupos = {
                "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L"
        };

        for (String grupo : grupos) {
            List<ClassificacaoGrupoDTO> classificacao =
                    classificacaoService.calcularClassificacaoGrupo(grupo);

            primeiros.add(classificacao.get(0));
            segundos.add(classificacao.get(1));
            terceiros.add(classificacao.get(2));
        }

        terceiros.sort(
                Comparator.comparing(ClassificacaoGrupoDTO::getPontos).reversed()
                        .thenComparing(ClassificacaoGrupoDTO::getSaldoGols, Comparator.reverseOrder())
                        .thenComparing(ClassificacaoGrupoDTO::getGolsPro, Comparator.reverseOrder())
                        .thenComparing(ClassificacaoGrupoDTO::getNomeSelecao)
        );

        List<ClassificacaoGrupoDTO> melhoresTerceiros = terceiros.subList(0, 8);

        criarJogoMataMata(buscarSelecao(primeiros.get(0)), buscarSelecao(melhoresTerceiros.get(7)), FaseCopa.DEZESSEIS_AVOS, "MetLife Stadium", 2026, 6, 28, 18, 0, 1);
        criarJogoMataMata(buscarSelecao(primeiros.get(1)), buscarSelecao(melhoresTerceiros.get(6)), FaseCopa.DEZESSEIS_AVOS, "SoFi Stadium", 2026, 6, 28, 21, 0, 2);
        criarJogoMataMata(buscarSelecao(primeiros.get(2)), buscarSelecao(melhoresTerceiros.get(5)), FaseCopa.DEZESSEIS_AVOS, "AT&T Stadium", 2026, 6, 29, 18, 0, 3);
        criarJogoMataMata(buscarSelecao(primeiros.get(3)), buscarSelecao(melhoresTerceiros.get(4)), FaseCopa.DEZESSEIS_AVOS, "Mercedes-Benz Stadium", 2026, 6, 29, 21, 0, 4);

        criarJogoMataMata(buscarSelecao(primeiros.get(4)), buscarSelecao(melhoresTerceiros.get(3)), FaseCopa.DEZESSEIS_AVOS, "Hard Rock Stadium", 2026, 6, 30, 18, 0, 5);
        criarJogoMataMata(buscarSelecao(primeiros.get(5)), buscarSelecao(melhoresTerceiros.get(2)), FaseCopa.DEZESSEIS_AVOS, "BC Place", 2026, 6, 30, 21, 0, 6);
        criarJogoMataMata(buscarSelecao(primeiros.get(6)), buscarSelecao(melhoresTerceiros.get(1)), FaseCopa.DEZESSEIS_AVOS, "BMO Field", 2026, 7, 1, 18, 0, 7);
        criarJogoMataMata(buscarSelecao(primeiros.get(7)), buscarSelecao(melhoresTerceiros.get(0)), FaseCopa.DEZESSEIS_AVOS, "Estadio Azteca", 2026, 7, 1, 21, 0, 8);

        criarJogoMataMata(buscarSelecao(primeiros.get(8)), buscarSelecao(segundos.get(9)), FaseCopa.DEZESSEIS_AVOS, "MetLife Stadium", 2026, 7, 2, 18, 0, 9);
        criarJogoMataMata(buscarSelecao(primeiros.get(9)), buscarSelecao(segundos.get(8)), FaseCopa.DEZESSEIS_AVOS, "SoFi Stadium", 2026, 7, 2, 21, 0, 10);
        criarJogoMataMata(buscarSelecao(primeiros.get(10)), buscarSelecao(segundos.get(11)), FaseCopa.DEZESSEIS_AVOS, "AT&T Stadium", 2026, 7, 3, 18, 0, 11);
        criarJogoMataMata(buscarSelecao(primeiros.get(11)), buscarSelecao(segundos.get(10)), FaseCopa.DEZESSEIS_AVOS, "Mercedes-Benz Stadium", 2026, 7, 3, 21, 0, 12);

        criarJogoMataMata(buscarSelecao(segundos.get(0)), buscarSelecao(segundos.get(1)), FaseCopa.DEZESSEIS_AVOS, "Hard Rock Stadium", 2026, 7, 4, 18, 0, 13);
        criarJogoMataMata(buscarSelecao(segundos.get(2)), buscarSelecao(segundos.get(3)), FaseCopa.DEZESSEIS_AVOS, "BC Place", 2026, 7, 4, 21, 0, 14);
        criarJogoMataMata(buscarSelecao(segundos.get(4)), buscarSelecao(segundos.get(5)), FaseCopa.DEZESSEIS_AVOS, "BMO Field", 2026, 7, 5, 18, 0, 15);
        criarJogoMataMata(buscarSelecao(segundos.get(6)), buscarSelecao(segundos.get(7)), FaseCopa.DEZESSEIS_AVOS, "Estadio Azteca", 2026, 7, 5, 21, 0, 16);

        return "Dezesseis-avos gerados com sucesso.";
    }

    @Transactional
    public String gerarOitavas() {
        return gerarProximaFase(
                FaseCopa.DEZESSEIS_AVOS,
                FaseCopa.OITAVAS,
                16,
                "Oitavas geradas com sucesso.",
                List.of(
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 4, 14, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 4, 18, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 6, 21, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 6, 16, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 5, 17, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 5, 21, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 7, 17, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 7, 13, 0)
                )
        );
    }

    @Transactional
    public String gerarQuartas() {
        return gerarProximaFase(
                FaseCopa.OITAVAS,
                FaseCopa.QUARTAS,
                8,
                "Quartas geradas com sucesso.",
                List.of(
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 9, 17, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 10, 16, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 11, 18, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 11, 22, 0)
                )
        );
    }

    @Transactional
    public String gerarSemifinal() {
        return gerarProximaFase(
                FaseCopa.QUARTAS,
                FaseCopa.SEMIFINAL,
                4,
                "Semifinais geradas com sucesso.",
                List.of(
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 14, 16, 0),
                        new JogoMataMataConfig("Estadio a definir", 2026, 7, 15, 16, 0)
                )
        );
    }

    @Transactional
    public String gerarFinal() {
        if (jogoRepository.countByFase(FaseCopa.FINAL) > 0) {
            throw new BusinessException("A final ja foi gerada.");
        }

        List<Jogo> semifinais = jogosOrdenadosPorChave(FaseCopa.SEMIFINAL);

        if (semifinais.size() != 2) {
            throw new BusinessException("E necessario ter exatamente 2 semifinais para gerar a final.");
        }

        validarOrdemMataMata(semifinais, FaseCopa.SEMIFINAL);
        validarJogosFinalizados(semifinais, FaseCopa.SEMIFINAL);

        criarJogoMataMata(
                obterVencedor(semifinais.get(0)),
                obterVencedor(semifinais.get(1)),
                FaseCopa.FINAL,
                "Estadio a definir",
                2026,
                7,
                19,
                16,
                0,
                1
        );

        return "Final gerada com sucesso.";
    }

    @Transactional
    public String gerarTerceiroLugar() {
        if (jogoRepository.countByFase(FaseCopa.TERCEIRO_LUGAR) > 0) {
            throw new BusinessException("A disputa de terceiro lugar ja foi gerada.");
        }

        List<Jogo> semifinais = jogosOrdenadosPorChave(FaseCopa.SEMIFINAL);

        if (semifinais.size() != 2) {
            throw new BusinessException("E necessario ter exatamente 2 semifinais para gerar o terceiro lugar.");
        }

        validarOrdemMataMata(semifinais, FaseCopa.SEMIFINAL);
        validarJogosFinalizados(semifinais, FaseCopa.SEMIFINAL);

        criarJogoMataMata(
                obterPerdedor(semifinais.get(0)),
                obterPerdedor(semifinais.get(1)),
                FaseCopa.TERCEIRO_LUGAR,
                "Estadio a definir",
                2026,
                7,
                18,
                16,
                0,
                1
        );

        return "Disputa de terceiro lugar gerada com sucesso.";
    }

    private String gerarProximaFase(
            FaseCopa faseAnterior,
            FaseCopa faseNova,
            int quantidadeEsperadaFaseAnterior,
            String mensagemSucesso,
            List<JogoMataMataConfig> configuracoes
    ) {
        if (jogoRepository.countByFase(faseNova) > 0) {
            throw new BusinessException("A fase " + faseNova + " ja foi gerada.");
        }

        List<Jogo> jogosAnteriores = jogosOrdenadosPorChave(faseAnterior);

        if (jogosAnteriores.size() != quantidadeEsperadaFaseAnterior) {
            throw new BusinessException(
                    "Quantidade invalida de jogos em " + faseAnterior +
                            ". Esperado: " + quantidadeEsperadaFaseAnterior +
                            ". Encontrado: " + jogosAnteriores.size()
            );
        }

        if (configuracoes.size() != quantidadeEsperadaFaseAnterior / 2) {
            throw new BusinessException("Configuracao invalida para gerar " + faseNova + ".");
        }

        validarOrdemMataMata(jogosAnteriores, faseAnterior);
        validarJogosFinalizados(jogosAnteriores, faseAnterior);

        for (int i = 0; i < jogosAnteriores.size(); i += 2) {
            int ordemNovaFase = (i / 2) + 1;
            JogoMataMataConfig config = configuracoes.get(i / 2);

            criarJogoMataMata(
                    obterVencedor(jogosAnteriores.get(i)),
                    obterVencedor(jogosAnteriores.get(i + 1)),
                    faseNova,
                    config.nomeEstadio(),
                    config.ano(),
                    config.mes(),
                    config.dia(),
                    config.hora(),
                    config.minuto(),
                    ordemNovaFase
            );
        }

        return mensagemSucesso;
    }

    private List<Jogo> jogosOrdenadosPorChave(FaseCopa fase) {
        List<Jogo> jogos = jogoRepository.findByFaseOrderByOrdemMataMataAsc(fase);
        jogos.sort(Comparator.comparing(Jogo::getOrdemMataMata, Comparator.nullsLast(Integer::compareTo)));
        return jogos;
    }

    private void validarOrdemMataMata(List<Jogo> jogos, FaseCopa fase) {
        for (int i = 0; i < jogos.size(); i++) {
            Integer ordemEsperada = i + 1;
            Integer ordemAtual = jogos.get(i).getOrdemMataMata();

            if (!ordemEsperada.equals(ordemAtual)) {
                throw new BusinessException(
                        "A fase " + fase + " precisa ter ordem_mata_mata preenchida de 1 ate " + jogos.size() + "."
                );
            }
        }
    }

    private void validarGruposFinalizados() {
        String[] grupos = {
                "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L"
        };

        for (String grupo : grupos) {
            List<Jogo> jogos = jogoRepository.findByFaseAndGrupoOrderByDataHoraAsc(
                    FaseCopa.GRUPOS,
                    grupo
            );

            if (jogos.size() < 6) {
                throw new BusinessException("O grupo " + grupo + " ainda nao possui 6 jogos.");
            }

            boolean existePendente = jogos.stream()
                    .anyMatch(jogo -> !Boolean.TRUE.equals(jogo.getFinalizado()));

            if (existePendente) {
                throw new BusinessException("Ainda existem jogos pendentes no grupo " + grupo + ".");
            }
        }
    }

    private void validarJogosFinalizados(List<Jogo> jogos, FaseCopa fase) {
        boolean existePendente = jogos.stream()
                .anyMatch(jogo -> !Boolean.TRUE.equals(jogo.getFinalizado()));

        if (existePendente) {
            throw new BusinessException("Ainda existem jogos pendentes na fase " + fase + ".");
        }
    }

    private Selecao buscarSelecao(ClassificacaoGrupoDTO dto) {
        return selecaoRepository.findById(dto.getSelecaoId())
                .orElseThrow(() -> new BusinessException("Selecao nao encontrada"));
    }

    private Selecao obterVencedor(Jogo jogo) {
        if (!Boolean.TRUE.equals(jogo.getFinalizado())) {
            throw new BusinessException("Existe jogo nao finalizado na fase " + jogo.getFase());
        }

        if (jogo.getGolsCasa() > jogo.getGolsVisitante()) {
            return jogo.getTimeCasa();
        }

        if (jogo.getGolsVisitante() > jogo.getGolsCasa()) {
            return jogo.getTimeVisitante();
        }

        if (jogo.getPenaltisCasa() == null || jogo.getPenaltisVisitante() == null) {
            throw new BusinessException("Jogo empatado sem resultado dos penaltis.");
        }

        if (jogo.getPenaltisCasa() > jogo.getPenaltisVisitante()) {
            return jogo.getTimeCasa();
        }

        return jogo.getTimeVisitante();
    }

    private Selecao obterPerdedor(Jogo jogo) {
        Selecao vencedor = obterVencedor(jogo);

        if (vencedor.getId().equals(jogo.getTimeCasa().getId())) {
            return jogo.getTimeVisitante();
        }

        return jogo.getTimeCasa();
    }

    private Estadio buscarOuCriarEstadio(String nomeEstadio) {
        return estadioRepository.findByNome(nomeEstadio)
                .orElseGet(() -> {
                    Estadio estadio = new Estadio();
                    estadio.setNome(nomeEstadio);
                    estadio.setCidade(null);
                    estadio.setPais(null);
                    return estadioRepository.save(estadio);
                });
    }

    private void criarJogoMataMata(
            Selecao casa,
            Selecao visitante,
            FaseCopa fase,
            String nomeEstadio,
            int ano,
            int mes,
            int dia,
            int hora,
            int minuto,
            int ordemMataMata
    ) {
        Estadio estadio = buscarOuCriarEstadio(nomeEstadio);

        Jogo jogo = new Jogo();
        jogo.setTimeCasa(casa);
        jogo.setTimeVisitante(visitante);
        jogo.setEstadio(estadio);
        jogo.setFase(fase);
        jogo.setGrupo(null);
        jogo.setDataHora(LocalDateTime.of(ano, mes, dia, hora, minuto));
        jogo.setFinalizado(false);
        jogo.setGolsCasa(null);
        jogo.setGolsVisitante(null);
        jogo.setPenaltisCasa(null);
        jogo.setPenaltisVisitante(null);
        jogo.setOrdemMataMata(ordemMataMata);

        jogoRepository.save(jogo);
    }

    private record JogoMataMataConfig(
            String nomeEstadio,
            int ano,
            int mes,
            int dia,
            int hora,
            int minuto
    ) {
    }
}
