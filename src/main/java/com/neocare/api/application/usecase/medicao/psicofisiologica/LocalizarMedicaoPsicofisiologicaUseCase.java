package com.neocare.api.application.usecase.medicao.psicofisiologica;

import com.neocare.api.domain.model.MedicaoPsicofisiologica;

public interface LocalizarMedicaoPsicofisiologicaUseCase {
    MedicaoPsicofisiologica execute(Long id);
}
