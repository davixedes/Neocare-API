package com.neocare.api.interfaces.dto.output;

import java.time.LocalDateTime;

public record ResultadoPredicaoOutDto(
        Double score,
        String predicao,
        LocalDateTime analisadoEm
) {}
