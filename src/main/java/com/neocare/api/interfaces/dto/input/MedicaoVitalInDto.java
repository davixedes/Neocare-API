package com.neocare.api.interfaces.dto.input;

import com.neocare.api.domain.enums.TipoMedicao;
import jakarta.validation.constraints.*;

public record MedicaoVitalInDto(
        @NotNull(message = "ID do usuário é obrigatório")
        @Positive(message = "ID do usuário deve ser positivo")
        Long idUsuario,

        @NotNull(message = "ID do dispositivo é obrigatório")
        @Positive(message = "ID do dispositivo deve ser positivo")
        Long idDispositivo,

        @NotNull(message = "Tipo de medição é obrigatório")
        TipoMedicao tipoMedicao,

        @NotNull(message = "Batimentos por minuto são obrigatórios")
        @Min(value = 30, message = "BPM mínimo: 30")
        @Max(value = 250, message = "BPM máximo: 250")
        Integer batimentosPorMinuto,

        @NotNull(message = "Oxigenação sanguínea é obrigatória")
        @DecimalMin(value = "50.0", message = "SpO2 mínimo: 50.0%")
        @DecimalMax(value = "100.0", message = "SpO2 máximo: 100.0%")
        Double oxigenacaoSangue,

        @NotNull(message = "Pressão sistólica é obrigatória")
        @Min(value = 60, message = "Pressão sistólica mínima: 60 mmHg")
        @Max(value = 300, message = "Pressão sistólica máxima: 300 mmHg")
        Integer pressaoSistolica,

        @NotNull(message = "Pressão diastólica é obrigatória")
        @Min(value = 30, message = "Pressão diastólica mínima: 30 mmHg")
        @Max(value = 180, message = "Pressão diastólica máxima: 180 mmHg")
        Integer pressaoDiastolica
) {
}
