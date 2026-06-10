package com.bolao.copa2026.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bolao.copa2026.model.Estadio;
import com.bolao.copa2026.model.FaseCopa;
import com.bolao.copa2026.model.Jogo;
import com.bolao.copa2026.model.Selecao;
import com.bolao.copa2026.repository.EstadioRepository;
import com.bolao.copa2026.repository.JogoRepository;
import com.bolao.copa2026.repository.SelecaoRepository;

@Component
public class CargaInicialCopa2026 implements CommandLineRunner {

    private final SelecaoRepository selecaoRepository;
    private final EstadioRepository estadioRepository;
    private final JogoRepository jogoRepository;

    public CargaInicialCopa2026(
            SelecaoRepository selecaoRepository,
            EstadioRepository estadioRepository,
            JogoRepository jogoRepository
    ) {
        this.selecaoRepository = selecaoRepository;
        this.estadioRepository = estadioRepository;
        this.jogoRepository = jogoRepository;
    }

    @Override
    public void run(String... args) {
        if (selecaoRepository.count() == 0) {
            carregarSelecoes();
        }

        if (estadioRepository.count() == 0) {
            carregarEstadios();
        }

        if (jogoRepository.count() == 0) {
            carregarJogosFaseDeGrupos();
        }
    }

    private void carregarSelecoes() {
        criarSelecao("México", "MEX", "A", "https://flagcdn.com/w80/mx.png");
        criarSelecao("África do Sul", "RSA", "A", "https://flagcdn.com/w80/za.png");
        criarSelecao("Coreia do Sul", "KOR", "A", "https://flagcdn.com/w80/kr.png");
        criarSelecao("República Tcheca", "CZE", "A", "https://flagcdn.com/w80/cz.png");

        criarSelecao("Canadá", "CAN", "B", "https://flagcdn.com/w80/ca.png");
        criarSelecao("Catar", "QAT", "B", "https://flagcdn.com/w80/qa.png");
        criarSelecao("Suíça", "SUI", "B", "https://flagcdn.com/w80/ch.png");
        criarSelecao("Bósnia-Herzegovina", "BIH", "B", "https://flagcdn.com/w80/ba.png");

        criarSelecao("Brasil", "BRA", "C", "https://flagcdn.com/w80/br.png");
        criarSelecao("Marrocos", "MAR", "C", "https://flagcdn.com/w80/ma.png");
        criarSelecao("Escócia", "SCO", "C", "https://flagcdn.com/w80/gb-sct.png");
        criarSelecao("Haiti", "HAI", "C", "https://flagcdn.com/w80/ht.png");

        criarSelecao("Estados Unidos", "USA", "D", "https://flagcdn.com/w80/us.png");
        criarSelecao("Austrália", "AUS", "D", "https://flagcdn.com/w80/au.png");
        criarSelecao("Turquia", "TUR", "D", "https://flagcdn.com/w80/tr.png");
        criarSelecao("Paraguai", "PAR", "D", "https://flagcdn.com/w80/py.png");

        criarSelecao("Alemanha", "GER", "E", "https://flagcdn.com/w80/de.png");
        criarSelecao("Curaçao", "CUW", "E", "https://flagcdn.com/w80/cw.png");
        criarSelecao("Equador", "ECU", "E", "https://flagcdn.com/w80/ec.png");
        criarSelecao("Costa do Marfim", "CIV", "E", "https://flagcdn.com/w80/ci.png");

        criarSelecao("Holanda", "NED", "F", "https://flagcdn.com/w80/nl.png");
        criarSelecao("Japão", "JPN", "F", "https://flagcdn.com/w80/jp.png");
        criarSelecao("Tunísia", "TUN", "F", "https://flagcdn.com/w80/tn.png");
        criarSelecao("Suécia", "SWE", "F", "https://flagcdn.com/w80/se.png");

        criarSelecao("Bélgica", "BEL", "G", "https://flagcdn.com/w80/be.png");
        criarSelecao("Egito", "EGY", "G", "https://flagcdn.com/w80/eg.png");
        criarSelecao("Nova Zelândia", "NZL", "G", "https://flagcdn.com/w80/nz.png");
        criarSelecao("Irã", "IRN", "G", "https://flagcdn.com/w80/ir.png");

        criarSelecao("Espanha", "ESP", "H", "https://flagcdn.com/w80/es.png");
        criarSelecao("Cabo Verde", "CPV", "H", "https://flagcdn.com/w80/cv.png");
        criarSelecao("Uruguai", "URU", "H", "https://flagcdn.com/w80/uy.png");
        criarSelecao("Arábia Saudita", "KSA", "H", "https://flagcdn.com/w80/sa.png");

        criarSelecao("França", "FRA", "I", "https://flagcdn.com/w80/fr.png");
        criarSelecao("Senegal", "SEN", "I", "https://flagcdn.com/w80/sn.png");
        criarSelecao("Noruega", "NOR", "I", "https://flagcdn.com/w80/no.png");
        criarSelecao("Iraque", "IRQ", "I", "https://flagcdn.com/w80/iq.png");

        criarSelecao("Argentina", "ARG", "J", "https://flagcdn.com/w80/ar.png");
        criarSelecao("Áustria", "AUT", "J", "https://flagcdn.com/w80/at.png");
        criarSelecao("Argélia", "ALG", "J", "https://flagcdn.com/w80/dz.png");
        criarSelecao("Jordânia", "JOR", "J", "https://flagcdn.com/w80/jo.png");

        criarSelecao("Portugal", "POR", "K", "https://flagcdn.com/w80/pt.png");
        criarSelecao("R. D. Congo", "COD", "K", "https://flagcdn.com/w80/cd.png");
        criarSelecao("Colômbia", "COL", "K", "https://flagcdn.com/w80/co.png");
        criarSelecao("Uzbequistão", "UZB", "K", "https://flagcdn.com/w80/uz.png");

        criarSelecao("Inglaterra", "ENG", "L", "https://flagcdn.com/w80/gb-eng.png");
        criarSelecao("Croácia", "CRO", "L", "https://flagcdn.com/w80/hr.png");
        criarSelecao("Gana", "GHA", "L", "https://flagcdn.com/w80/gh.png");
        criarSelecao("Panamá", "PAN", "L", "https://flagcdn.com/w80/pa.png");
    }

    private void carregarEstadios() {
        criarEstadio("Estádio Azteca", "Cidade do México", "México");
        criarEstadio("MetLife Stadium", "Nova Jersey", "Estados Unidos");
        criarEstadio("SoFi Stadium", "Los Angeles", "Estados Unidos");
        criarEstadio("AT&T Stadium", "Dallas", "Estados Unidos");
        criarEstadio("Mercedes-Benz Stadium", "Atlanta", "Estados Unidos");
        criarEstadio("Hard Rock Stadium", "Miami", "Estados Unidos");
        criarEstadio("BC Place", "Vancouver", "Canadá");
        criarEstadio("BMO Field", "Toronto", "Canadá");
    }

    private void carregarJogosFaseDeGrupos() {
        criarJogo("A", "MEX", "RSA", "Estádio Azteca", 2026, 6, 11, 16, 0);
        criarJogo("A", "KOR", "CZE", "MetLife Stadium", 2026, 6, 11, 22, 0);
        criarJogo("A", "MEX", "KOR", "Estádio Azteca", 2026, 6, 18, 19, 0);
        criarJogo("A", "RSA", "CZE", "AT&T Stadium", 2026, 6, 18, 22, 0);
        criarJogo("A", "MEX", "CZE", "Estádio Azteca", 2026, 6, 24, 22, 0);
        criarJogo("A", "RSA", "KOR", "SoFi Stadium", 2026, 6, 24, 22, 0);

        criarJogo("B", "CAN", "QAT", "BMO Field", 2026, 6, 12, 16, 0);
        criarJogo("B", "SUI", "BIH", "BC Place", 2026, 6, 12, 19, 0);
        criarJogo("B", "CAN", "SUI", "BMO Field", 2026, 6, 18, 19, 0);
        criarJogo("B", "QAT", "BIH", "BC Place", 2026, 6, 18, 22, 0);
        criarJogo("B", "CAN", "BIH", "BMO Field", 2026, 6, 24, 23, 0);
        criarJogo("B", "QAT", "SUI", "BC Place", 2026, 6, 24, 23, 0);

        criarJogo("C", "BRA", "MAR", "MetLife Stadium", 2026, 6, 13, 19, 0);
        criarJogo("C", "SCO", "HAI", "SoFi Stadium", 2026, 6, 13, 22, 0);
        criarJogo("C", "BRA", "SCO", "MetLife Stadium", 2026, 6, 19, 19, 0);
        criarJogo("C", "MAR", "HAI", "AT&T Stadium", 2026, 6, 19, 22, 0);
        criarJogo("C", "BRA", "HAI", "MetLife Stadium", 2026, 6, 24, 23, 0);
        criarJogo("C", "MAR", "SCO", "SoFi Stadium", 2026, 6, 24, 23, 0);

        criarJogo("D", "USA", "AUS", "SoFi Stadium", 2026, 6, 14, 22, 0);
        criarJogo("D", "TUR", "PAR", "AT&T Stadium", 2026, 6, 14, 19, 0);
        criarJogo("D", "USA", "TUR", "SoFi Stadium", 2026, 6, 20, 19, 0);
        criarJogo("D", "AUS", "PAR", "AT&T Stadium", 2026, 6, 20, 22, 0);
        criarJogo("D", "USA", "PAR", "SoFi Stadium", 2026, 6, 25, 23, 0);
        criarJogo("D", "AUS", "TUR", "AT&T Stadium", 2026, 6, 25, 23, 0);

        criarJogo("E", "GER", "CUW", "Mercedes-Benz Stadium", 2026, 6, 14, 16, 0);
        criarJogo("E", "ECU", "CIV", "Hard Rock Stadium", 2026, 6, 14, 19, 0);
        criarJogo("E", "GER", "ECU", "Mercedes-Benz Stadium", 2026, 6, 20, 19, 0);
        criarJogo("E", "CUW", "CIV", "Hard Rock Stadium", 2026, 6, 20, 22, 0);
        criarJogo("E", "GER", "CIV", "Mercedes-Benz Stadium", 2026, 6, 25, 23, 0);
        criarJogo("E", "CUW", "ECU", "Hard Rock Stadium", 2026, 6, 25, 23, 0);

        criarJogo("F", "NED", "JPN", "AT&T Stadium", 2026, 6, 14, 17, 0);
        criarJogo("F", "TUN", "SWE", "Mercedes-Benz Stadium", 2026, 6, 14, 20, 0);
        criarJogo("F", "NED", "TUN", "AT&T Stadium", 2026, 6, 20, 18, 0);
        criarJogo("F", "JPN", "SWE", "Mercedes-Benz Stadium", 2026, 6, 20, 20, 0);
        criarJogo("F", "NED", "SWE", "AT&T Stadium", 2026, 6, 25, 20, 0);
        criarJogo("F", "JPN", "TUN", "Mercedes-Benz Stadium", 2026, 6, 25, 20, 0);

        criarJogo("G", "BEL", "EGY", "MetLife Stadium", 2026, 6, 15, 16, 0);
        criarJogo("G", "NZL", "IRN", "AT&T Stadium", 2026, 6, 15, 22, 0);
        criarJogo("G", "BEL", "NZL", "MetLife Stadium", 2026, 6, 21, 16, 0);
        criarJogo("G", "EGY", "IRN", "AT&T Stadium", 2026, 6, 21, 22, 0);
        criarJogo("G", "BEL", "IRN", "MetLife Stadium", 2026, 6, 27, 18, 0);
        criarJogo("G", "EGY", "NZL", "AT&T Stadium", 2026, 6, 27, 18, 0);

        criarJogo("H", "ESP", "CPV", "SoFi Stadium", 2026, 6, 15, 13, 0);
        criarJogo("H", "URU", "KSA", "MetLife Stadium", 2026, 6, 15, 16, 0);
        criarJogo("H", "ESP", "URU", "SoFi Stadium", 2026, 6, 21, 13, 0);
        criarJogo("H", "CPV", "KSA", "MetLife Stadium", 2026, 6, 21, 19, 0);
        criarJogo("H", "ESP", "KSA", "SoFi Stadium", 2026, 6, 26, 21, 0);
        criarJogo("H", "CPV", "URU", "MetLife Stadium", 2026, 6, 26, 21, 0);

        criarJogo("I", "FRA", "SEN", "MetLife Stadium", 2026, 6, 18, 18, 0);
        criarJogo("I", "NOR", "IRQ", "SoFi Stadium", 2026, 6, 18, 19, 0);
        criarJogo("I", "FRA", "NOR", "MetLife Stadium", 2026, 6, 22, 18, 0);
        criarJogo("I", "SEN", "IRQ", "SoFi Stadium", 2026, 6, 22, 21, 0);
        criarJogo("I", "FRA", "IRQ", "MetLife Stadium", 2026, 6, 26, 16, 0);
        criarJogo("I", "SEN", "NOR", "SoFi Stadium", 2026, 6, 26, 16, 0);

        criarJogo("J", "ARG", "AUT", "AT&T Stadium", 2026, 6, 16, 22, 0);
        criarJogo("J", "ALG", "JOR", "Mercedes-Benz Stadium", 2026, 6, 17, 13, 0);
        criarJogo("J", "ARG", "ALG", "AT&T Stadium", 2026, 6, 21, 13, 0);
        criarJogo("J", "AUT", "JOR", "Mercedes-Benz Stadium", 2026, 6, 22, 13, 0);
        criarJogo("J", "ARG", "JOR", "AT&T Stadium", 2026, 6, 27, 13, 0);
        criarJogo("J", "AUT", "ALG", "Mercedes-Benz Stadium", 2026, 6, 27, 13, 0);

        criarJogo("K", "POR", "COD", "Mercedes-Benz Stadium", 2026, 6, 17, 14, 0);
        criarJogo("K", "COL", "UZB", "Hard Rock Stadium", 2026, 6, 17, 20, 0);
        criarJogo("K", "POR", "COL", "Mercedes-Benz Stadium", 2026, 6, 23, 14, 0);
        criarJogo("K", "COD", "UZB", "Hard Rock Stadium", 2026, 6, 23, 20, 0);
        criarJogo("K", "POR", "UZB", "Mercedes-Benz Stadium", 2026, 6, 27, 20, 0);
        criarJogo("K", "COD", "COL", "Hard Rock Stadium", 2026, 6, 27, 20, 0);

        criarJogo("L", "ENG", "CRO", "AT&T Stadium", 2026, 6, 17, 17, 0);
        criarJogo("L", "GHA", "PAN", "BMO Field", 2026, 6, 17, 20, 0);
        criarJogo("L", "ENG", "GHA", "AT&T Stadium", 2026, 6, 23, 17, 0);
        criarJogo("L", "CRO", "PAN", "BMO Field", 2026, 6, 23, 20, 0);
        criarJogo("L", "ENG", "PAN", "AT&T Stadium", 2026, 6, 27, 17, 0);
        criarJogo("L", "CRO", "GHA", "BMO Field", 2026, 6, 27, 17, 0);
    }

    private void criarSelecao(String nome, String siglaFifa, String grupo, String bandeiraUrl) {
        Selecao selecao = new Selecao();
        selecao.setNome(nome);
        selecao.setSiglaFifa(siglaFifa);
        selecao.setGrupo(grupo);
        selecao.setBandeiraUrl(bandeiraUrl);
        selecaoRepository.save(selecao);
    }

    private void criarEstadio(String nome, String cidade, String pais) {
        Estadio estadio = new Estadio();
        estadio.setNome(nome);
        estadio.setCidade(cidade);
        estadio.setPais(pais);
        estadioRepository.save(estadio);
    }

    private void criarJogo(String grupo, String siglaCasa, String siglaVisitante, String nomeEstadio,
                           int ano, int mes, int dia, int hora, int minuto) {
        Selecao timeCasa = selecaoRepository.findBySiglaFifa(siglaCasa)
                .orElseThrow(() -> new RuntimeException("Seleção não encontrada: " + siglaCasa));

        Selecao timeVisitante = selecaoRepository.findBySiglaFifa(siglaVisitante)
                .orElseThrow(() -> new RuntimeException("Seleção não encontrada: " + siglaVisitante));

        Estadio estadio = estadioRepository.findByNome(nomeEstadio)
                .orElseThrow(() -> new RuntimeException("Estádio não encontrado: " + nomeEstadio));

        Jogo jogo = new Jogo();
        jogo.setGrupo(grupo);
        jogo.setTimeCasa(timeCasa);
        jogo.setTimeVisitante(timeVisitante);
        jogo.setEstadio(estadio);
        jogo.setDataHora(LocalDateTime.of(ano, mes, dia, hora, minuto));
        jogo.setFase(FaseCopa.GRUPOS);
        jogo.setFinalizado(false);
        jogo.setGolsCasa(null);
        jogo.setGolsVisitante(null);

        jogoRepository.save(jogo);
    }
}