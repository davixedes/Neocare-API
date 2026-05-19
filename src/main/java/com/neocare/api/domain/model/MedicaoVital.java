package com.neocare.api.domain.model;

import com.neocare.api.domain.enums.TipoMedicao;
import com.neocare.api.domain.exception.ValidacaoDominioException;

import java.time.LocalDateTime;

public class MedicaoVital extends Medicao {

    private Integer batimentosPorMinuto;
    private Double oxigenacaoSangue;
    private Integer pressaoSistolica;
    private Integer pressaoDiastolica;

    public MedicaoVital(Long idUsuario, Long idDispositivo, TipoMedicao tipoMedicao,
                        Integer batimentosPorMinuto, Double oxigenacaoSangue,
                        Integer pressaoSistolica, Integer pressaoDiastolica) {
        super(idUsuario, idDispositivo, tipoMedicao);
        validar(batimentosPorMinuto, oxigenacaoSangue, pressaoSistolica, pressaoDiastolica);
        this.batimentosPorMinuto = batimentosPorMinuto;
        this.oxigenacaoSangue = oxigenacaoSangue;
        this.pressaoSistolica = pressaoSistolica;
        this.pressaoDiastolica = pressaoDiastolica;
    }

    public MedicaoVital(Long id, Long idUsuario, Long idDispositivo, LocalDateTime dataMedicao,
                        TipoMedicao tipoMedicao, Integer batimentosPorMinuto, Double oxigenacaoSangue,
                        Integer pressaoSistolica, Integer pressaoDiastolica) {
        super(id, idUsuario, idDispositivo, dataMedicao, tipoMedicao);
        validar(batimentosPorMinuto, oxigenacaoSangue, pressaoSistolica, pressaoDiastolica);
        this.batimentosPorMinuto = batimentosPorMinuto;
        this.oxigenacaoSangue = oxigenacaoSangue;
        this.pressaoSistolica = pressaoSistolica;
        this.pressaoDiastolica = pressaoDiastolica;
    }

    private void validar(Integer bpm, Double spo2, Integer sistolica, Integer diastolica) {
        if (bpm != null && (bpm < 30 || bpm > 250)) {
            throw new ValidacaoDominioException("Batimentos por minuto fora do intervalo clínico (30-250 bpm)");
        }
        if (spo2 != null && (spo2 < 50.0 || spo2 > 100.0)) {
            throw new ValidacaoDominioException("Oxigenação sanguínea fora do intervalo válido (50-100%)");
        }
        if (sistolica != null && (sistolica < 60 || sistolica > 300)) {
            throw new ValidacaoDominioException("Pressão sistólica fora do intervalo clínico (60-300 mmHg)");
        }
        if (diastolica != null && (diastolica < 30 || diastolica > 180)) {
            throw new ValidacaoDominioException("Pressão diastólica fora do intervalo clínico (30-180 mmHg)");
        }
        if (sistolica != null && diastolica != null && diastolica >= sistolica) {
            throw new ValidacaoDominioException("Pressão diastólica deve ser menor que a sistólica");
        }
    }

    public Integer getBatimentosPorMinuto() { return batimentosPorMinuto; }
    public Double getOxigenacaoSangue() { return oxigenacaoSangue; }
    public Integer getPressaoSistolica() { return pressaoSistolica; }
    public Integer getPressaoDiastolica() { return pressaoDiastolica; }
}
