package com.neocare.api.infrastructure.repository;

import com.neocare.api.infrastructure.entity.JpaMetricaEstresseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaMetricaEstresseRepository extends JpaRepository<JpaMetricaEstresseEntity, Long> {

    Optional<JpaMetricaEstresseEntity> findByMedicaoPsicofisiologicaEntityId(Long medicaoId);

    @Query("SELECT m FROM JpaMetricaEstresseEntity m " +
           "WHERE m.medicaoPsicofisiologicaEntity.jpaUsuarioEntity.id = :usuarioId " +
           "ORDER BY m.dataMetrica DESC")
    List<JpaMetricaEstresseEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
