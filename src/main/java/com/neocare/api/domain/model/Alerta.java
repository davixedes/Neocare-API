package com.neocare.api.domain.model;

import com.neocare.api.domain.enums.Severidade;
import com.neocare.api.domain.enums.TipoAlerta;

import java.time.LocalDateTime;
import java.util.Optional;

public class Alerta {

    private Long id;

    private Long usuarioId;

    private Long medicaoId;

    private TipoAlerta tipoAlerta;

    private String valorDetectado;

    private Severidade severidade;

    private String mensagem;

    private LocalDateTime dataNotificacao;

    private boolean lido;

    public Alerta(Long usuarioId, Long medicaoId, TipoAlerta tipoAlerta, String valorDetectado, Severidade severidade, String mensagem, LocalDateTime dataNotificacao) {
        this.usuarioId = usuarioId;
        this.medicaoId = medicaoId;
        this.tipoAlerta = tipoAlerta;
        this.valorDetectado = valorDetectado;
        this.severidade = severidade;
        this.mensagem = mensagem;
        this.dataNotificacao = dataNotificacao;
        this.lido = false;
    }

    public Alerta(Long id, Long usuarioId, Long medicaoId, TipoAlerta tipoAlerta, String valorDetectado, Severidade severidade, String mensagem, LocalDateTime dataNotificacao) {
        this(id, usuarioId, medicaoId, tipoAlerta, valorDetectado, severidade, mensagem, dataNotificacao, false);
    }

    public Alerta(Long id, Long usuarioId, Long medicaoId, TipoAlerta tipoAlerta, String valorDetectado, Severidade severidade, String mensagem, LocalDateTime dataNotificacao, boolean lido) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.medicaoId = medicaoId;
        this.tipoAlerta = tipoAlerta;
        this.valorDetectado = valorDetectado;
        this.severidade = severidade;
        this.mensagem = mensagem;
        this.dataNotificacao = dataNotificacao;
        this.lido = lido;
    }

    public static Optional<Alerta> avaliarEstresse(MedicaoEstresse medicao) {
        Double hrv = medicao.getVariacaoFrequenciaCardiaca();
        Double gsr = medicao.getCondutividadePele();

        if (hrv == null || gsr == null) {
            return Optional.empty();
        }

        boolean hrvCritico = hrv < 20.0;
        boolean gsrCritico = gsr > 10.0;

        if (!hrvCritico && !gsrCritico) {
            return Optional.empty();
        }

        Severidade severidade = (hrv < 15.0 || gsr > 15.0) ? Severidade.ALTA : Severidade.MODERADA;
        String valor = "HRV=" + hrv + " GSR=" + gsr;

        return Optional.of(new Alerta(
                medicao.getIdUsuario(),
                medicao.getId(),
                TipoAlerta.ESTRESSE,
                valor,
                severidade,
                "Nível de estresse elevado detectado.",
                LocalDateTime.now()
        ));
    }

    public static Optional<Alerta> avaliarVital(MedicaoVital medicao) {
        Integer bpm = medicao.getBatimentosPorMinuto();
        Double spo2 = medicao.getOxigenacaoSangue();
        Integer sistolica = medicao.getPressaoSistolica();
        Integer diastolica = medicao.getPressaoDiastolica();

        boolean pressaoCritica = sistolica != null && sistolica > 180;
        boolean spo2Critico = spo2 != null && spo2 < 92.0;
        boolean bpmCritico = bpm != null && (bpm > 150 || bpm < 40);

        if (!pressaoCritica && !spo2Critico && !bpmCritico) {
            return Optional.empty();
        }

        Severidade severidade;
        if (pressaoCritica || spo2Critico) {
            severidade = Severidade.ALTA;
        } else {
            severidade = Severidade.MODERADA;
        }

        String valor = "BPM=" + bpm + " SpO2=" + spo2 + " PA=" + sistolica + "/" + diastolica;

        return Optional.of(new Alerta(
                medicao.getIdUsuario(),
                medicao.getId(),
                TipoAlerta.AVC,
                valor,
                severidade,
                "Sinais vitais críticos detectados.",
                LocalDateTime.now()
        ));
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getMedicaoId() {
        return medicaoId;
    }

    public TipoAlerta getTipoAlerta() {
        return tipoAlerta;
    }

    public String getValorDetectado() {
        return valorDetectado;
    }

    public Severidade getSeveridade() {
        return severidade;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataNotificacao() {
        return dataNotificacao;
    }

    public boolean isLido() {
        return lido;
    }

    public void marcarComoLido() {
        this.lido = true;
    }

    public static Optional<Alerta> avaliarPredicao(ResultadoPredicao resultado, Long usuarioId, TipoAlerta tipoAlerta) {
        if (resultado == null || resultado.getPredicao() == null || resultado.getScore() == null) {
            return Optional.empty();
        }
        if ("NORMAL".equalsIgnoreCase(resultado.getPredicao())) {
            return Optional.empty();
        }

        double score = resultado.getScore();
        Severidade severidade;
        if (score >= 0.8) {
            severidade = Severidade.ALTA;
        } else if (score >= 0.5) {
            severidade = Severidade.MODERADA;
        } else {
            return Optional.empty();
        }

        String valor = "Predicao=" + resultado.getPredicao() + " Score=" + score;
        return Optional.of(new Alerta(
                usuarioId,
                resultado.getMedicaoId(),
                tipoAlerta,
                valor,
                severidade,
                "Análise preditiva indicou risco: " + resultado.getPredicao(),
                LocalDateTime.now()
        ));
    }
}
