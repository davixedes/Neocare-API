package com.neocare.api.infrastructure.repository;

import com.neocare.api.infrastructure.entity.JpaResultadoPredicaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaResultadoPredicaoRepository extends JpaRepository<JpaResultadoPredicaoEntity, Long> {
    Optional<JpaResultadoPredicaoEntity> findByMedicaoId(Long medicaoId);
}
