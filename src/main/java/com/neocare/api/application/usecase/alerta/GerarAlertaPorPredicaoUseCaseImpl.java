package com.neocare.api.application.usecase.alerta;

import com.neocare.api.domain.enums.TipoAlerta;
import com.neocare.api.domain.logging.Logger;
import com.neocare.api.domain.model.Alerta;
import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.domain.repository.AlertaRepository;

import java.util.Optional;

public final class GerarAlertaPorPredicaoUseCaseImpl implements GerarAlertaPorPredicaoUseCase {

    private final AlertaRepository alertaRepository;
    private final Logger logger;

    public GerarAlertaPorPredicaoUseCaseImpl(AlertaRepository alertaRepository, Logger logger) {
        this.alertaRepository = alertaRepository;
        this.logger = logger;
    }

    @Override
    public Optional<Alerta> execute(ResultadoPredicao resultado, Long usuarioId, TipoAlerta tipoAlerta) {
        try {
            return Alerta.avaliarPredicao(resultado, usuarioId, tipoAlerta)
                    .map(alerta -> {
                        Alerta salvo = alertaRepository.save(alerta);
                        logger.info("Alerta gerado a partir da predição. Severidade: " + salvo.getSeveridade() + " | Tipo: " + salvo.getTipoAlerta());
                        return salvo;
                    });
        } catch (Exception e) {
            logger.error("Falha ao gerar alerta a partir da predição. Erro: " + e.getMessage(), e);
            return Optional.empty();
        }
    }
}
