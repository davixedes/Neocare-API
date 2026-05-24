package com.neocare.api.application.usecase.alerta;

import com.neocare.api.domain.enums.TipoAlerta;
import com.neocare.api.domain.model.Alerta;
import com.neocare.api.domain.model.ResultadoPredicao;

import java.util.Optional;

public interface GerarAlertaPorPredicaoUseCase {
    Optional<Alerta> execute(ResultadoPredicao resultado, Long usuarioId, TipoAlerta tipoAlerta);
}
