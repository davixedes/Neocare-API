package com.neocare.api.application.usecase.medicao.psicofisiologica;

import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.repository.MedicaoPsicofisiologicaRepository;

import java.util.List;

public final class ListarMedicoesPsicofisiologicasUseCaseImpl implements ListarMedicoesPsicofisiologicasUseCase {

    private final MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository;

    public ListarMedicoesPsicofisiologicasUseCaseImpl(MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository) {
        this.medicaoPsicofisiologicaRepository = medicaoPsicofisiologicaRepository;
    }

    @Override
    public List<MedicaoPsicofisiologica> porUsuario(Long usuarioId) {
        return medicaoPsicofisiologicaRepository.findByUsuarioId(usuarioId);
    }
}
