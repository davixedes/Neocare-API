package com.neocare.api.infrastructure.repository;

import com.neocare.api.infrastructure.entity.JpaMedicaoPsicofisiologicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaMedicaoPsicofisiologicaRepository extends JpaRepository<JpaMedicaoPsicofisiologicaEntity, Long> {
    List<JpaMedicaoPsicofisiologicaEntity> findByJpaUsuarioEntityIdOrderByDataMedicaoDesc(Long usuarioId);
}
