package com.neocare.api.interfaces.dto.output;

public record MedicaoVitalOutDto(
        MedicaoOutDto medicaoOutDto,
        Integer batimentosPorMinuto,
        Double oxigenacaoSangue,
        Integer pressaoSistolica,
        Integer pressaoDiastolica,
        DispositivoMedicaoOutDto dispositivoMedicaoOutDto,
        ResultadoPredicaoOutDto resultadoPredicao
) {
}
