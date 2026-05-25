package com.neocare.api.interfaces.dto.input;

import com.neocare.api.domain.enums.TipoMedicao;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MedicaoPsicofisiologicaInDto(
        @NotNull(message = "ID do usuário é obrigatório")
        @Positive(message = "ID do usuário deve ser positivo")
        Long idUsuario,

        @NotNull(message = "ID do dispositivo é obrigatório")
        @Positive(message = "ID do dispositivo deve ser positivo")
        Long idDispositivo,

        @NotNull(message = "Tipo de medição é obrigatório")
        TipoMedicao tipoMedicao,

        @NotNull(message = "Variação da frequência cardíaca é obrigatória")
        @DecimalMin(value = "0.0", message = "HRV mínimo: 0.0 ms")
        @DecimalMax(value = "300.0", message = "HRV máximo: 300.0 ms")
        Double variacaoFrequenciaCardiaca,

        @NotNull(message = "Condutividade da pele é obrigatória")
        @DecimalMin(value = "0.0", message = "GSR mínimo: 0.0 μS")
        @DecimalMax(value = "50.0", message = "GSR máximo: 50.0 μS")
        Double condutividadePele
) {
}
