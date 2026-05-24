package com.neocare.api.interfaces.dto.output;

import com.neocare.api.domain.enums.Severidade;
import com.neocare.api.domain.enums.TipoAlerta;

import java.time.LocalDateTime;

public record AlertaOutDto(
        Long id,
        Long usuarioId,
        Long medicaoId,
        TipoAlerta tipoAlerta,
        String valorDetectado,
        Severidade severidade,
        String mensagem,
        LocalDateTime dataNotificacao,
        boolean lido
) {
}
