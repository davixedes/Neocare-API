package com.neocare.api.interfaces.mapper;

import com.neocare.api.domain.model.MetricaEstresse;
import com.neocare.api.infrastructure.entity.JpaMedicaoPsicofisiologicaEntity;
import com.neocare.api.infrastructure.entity.JpaMetricaEstresseEntity;
import com.neocare.api.interfaces.dto.output.MetricaEstresseOutDto;

public final class MetricaEstresseMapper {

    private MetricaEstresseMapper() {
    }

    public static JpaMetricaEstresseEntity toEntity(MetricaEstresse metrica, JpaMedicaoPsicofisiologicaEntity medicaoEntity) {
        return new JpaMetricaEstresseEntity(
                medicaoEntity,
                metrica.getIndiceEstresse(),
                metrica.getCategoria(),
                metrica.getDataMetrica()
        );
    }

    public static MetricaEstresse toDomain(JpaMetricaEstresseEntity entity) {
        return new MetricaEstresse(
                entity.getId(),
                entity.getMedicaoPsicofisiologicaEntity().getId(),
                entity.getIndiceEstresse(),
                entity.getCategoria(),
                entity.getDataMetrica()
        );
    }

    public static MetricaEstresseOutDto toOutDto(MetricaEstresse metrica) {
        return new MetricaEstresseOutDto(
                metrica.getId(),
                metrica.getMedicaoEstresseId(),
                metrica.getIndiceEstresse(),
                metrica.getCategoria(),
                metrica.getDataMetrica()
        );
    }
}
