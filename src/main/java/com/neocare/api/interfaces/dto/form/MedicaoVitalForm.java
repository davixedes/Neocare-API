package com.neocare.api.interfaces.dto.form;

import jakarta.validation.constraints.*;

public class MedicaoVitalForm {

    @NotNull(message = "ID do dispositivo é obrigatório")
    @Positive(message = "ID do dispositivo deve ser positivo")
    private Long idDispositivo;

    @NotNull(message = "Batimentos por minuto são obrigatórios")
    @Min(value = 30, message = "BPM mínimo: 30")
    @Max(value = 250, message = "BPM máximo: 250")
    private Integer batimentosPorMinuto;

    @NotNull(message = "Oxigenação sanguínea é obrigatória")
    @DecimalMin(value = "50.0", message = "SpO2 mínimo: 50.0%")
    @DecimalMax(value = "100.0", message = "SpO2 máximo: 100.0%")
    private Double oxigenacaoSangue;

    @NotNull(message = "Pressão sistólica é obrigatória")
    @Min(value = 60, message = "Pressão sistólica mínima: 60 mmHg")
    @Max(value = 300, message = "Pressão sistólica máxima: 300 mmHg")
    private Integer pressaoSistolica;

    @NotNull(message = "Pressão diastólica é obrigatória")
    @Min(value = 30, message = "Pressão diastólica mínima: 30 mmHg")
    @Max(value = 180, message = "Pressão diastólica máxima: 180 mmHg")
    private Integer pressaoDiastolica;

    public MedicaoVitalForm() {
    }

    public Long getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(Long idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public Integer getBatimentosPorMinuto() {
        return batimentosPorMinuto;
    }

    public void setBatimentosPorMinuto(Integer batimentosPorMinuto) {
        this.batimentosPorMinuto = batimentosPorMinuto;
    }

    public Double getOxigenacaoSangue() {
        return oxigenacaoSangue;
    }

    public void setOxigenacaoSangue(Double oxigenacaoSangue) {
        this.oxigenacaoSangue = oxigenacaoSangue;
    }

    public Integer getPressaoSistolica() {
        return pressaoSistolica;
    }

    public void setPressaoSistolica(Integer pressaoSistolica) {
        this.pressaoSistolica = pressaoSistolica;
    }

    public Integer getPressaoDiastolica() {
        return pressaoDiastolica;
    }

    public void setPressaoDiastolica(Integer pressaoDiastolica) {
        this.pressaoDiastolica = pressaoDiastolica;
    }
}
