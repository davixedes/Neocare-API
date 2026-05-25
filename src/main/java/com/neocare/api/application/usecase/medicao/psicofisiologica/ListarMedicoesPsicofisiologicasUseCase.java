package com.neocare.api.application.usecase.medicao.psicofisiologica;

import com.neocare.api.domain.model.MedicaoPsicofisiologica;

import java.util.List;

public interface ListarMedicoesPsicofisiologicasUseCase {
    List<MedicaoPsicofisiologica> porUsuario(Long usuarioId);
}
