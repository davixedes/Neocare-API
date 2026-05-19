package com.neocare.api.interfaces.dto.form;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MedicaoEstresseForm {

    @NotNull(message = "ID do dispositivo é obrigatório")
    @Positive(message = "ID do dispositivo deve ser positivo")
    private Long idDispositivo;

    @NotNull(message = "Variação da frequência cardíaca é obrigatória")
    @DecimalMin(value = "0.0", message = "HRV mínimo: 0.0 ms")
    @DecimalMax(value = "300.0", message = "HRV máximo: 300.0 ms")
    private Double variacaoFrequenciaCardiaca;

    @NotNull(message = "Condutividade da pele é obrigatória")
    @DecimalMin(value = "0.0", message = "GSR mínimo: 0.0 μS")
    @DecimalMax(value = "50.0", message = "GSR máximo: 50.0 μS")
    private Double condutividadePele;

    public MedicaoEstresseForm() {
    }

    public Long getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(Long idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public Double getVariacaoFrequenciaCardiaca() {
        return variacaoFrequenciaCardiaca;
    }

    public void setVariacaoFrequenciaCardiaca(Double variacaoFrequenciaCardiaca) {
        this.variacaoFrequenciaCardiaca = variacaoFrequenciaCardiaca;
    }

    public Double getCondutividadePele() {
        return condutividadePele;
    }

    public void setCondutividadePele(Double condutividadePele) {
        this.condutividadePele = condutividadePele;
    }
}
