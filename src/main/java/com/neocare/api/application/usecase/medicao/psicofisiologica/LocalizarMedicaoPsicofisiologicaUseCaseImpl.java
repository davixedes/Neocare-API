package com.neocare.api.application.usecase.medicao.psicofisiologica;

import com.neocare.api.application.exception.EntidadeNaoEncontradaException;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.repository.MedicaoPsicofisiologicaRepository;

public final class LocalizarMedicaoPsicofisiologicaUseCaseImpl implements LocalizarMedicaoPsicofisiologicaUseCase {

    private final MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository;

    public LocalizarMedicaoPsicofisiologicaUseCaseImpl(MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository) {
        this.medicaoPsicofisiologicaRepository = medicaoPsicofisiologicaRepository;
    }

    @Override
    public MedicaoPsicofisiologica execute(Long id) {
        return medicaoPsicofisiologicaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Medição psicofisiológica com ID " + id + " não encontrada."));
    }
}
