package com.neocare.api.domain.repository;

import com.neocare.api.domain.model.Alerta;

import java.util.List;
import java.util.Optional;

public interface AlertaRepository {
    Alerta save(Alerta alerta);

    Optional<Alerta> findById(Long alertaId);

    List<Alerta> findByUsuarioId(Long usuarioId);

    List<Alerta> findAll();
}
