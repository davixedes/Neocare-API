package com.neocare.api.domain.repository;

import com.neocare.api.domain.model.MedicaoPsicofisiologica;

import java.util.List;
import java.util.Optional;

public interface MedicaoPsicofisiologicaRepository {
    MedicaoPsicofisiologica save(MedicaoPsicofisiologica medicaoPsicofisiologica);

    List<MedicaoPsicofisiologica> findByUsuarioId(Long usuarioId);

    Optional<MedicaoPsicofisiologica> findById(Long id);
}
