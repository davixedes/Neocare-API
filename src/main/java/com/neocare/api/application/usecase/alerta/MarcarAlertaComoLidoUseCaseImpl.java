package com.neocare.api.application.usecase.alerta;

import com.neocare.api.application.exception.EntidadeNaoEncontradaException;
import com.neocare.api.domain.model.Alerta;
import com.neocare.api.domain.repository.AlertaRepository;

public final class MarcarAlertaComoLidoUseCaseImpl implements MarcarAlertaComoLidoUseCase {

    private final AlertaRepository alertaRepository;

    public MarcarAlertaComoLidoUseCaseImpl(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @Override
    public void execute(Long alertaId) {
        Alerta alerta = alertaRepository.findById(alertaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Alerta com ID " + alertaId + " não encontrado."));
        alerta.marcarComoLido();
        alertaRepository.save(alerta);
    }
}
