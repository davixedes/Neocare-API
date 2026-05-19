package com.neocare.api.interfaces.mapper;

import com.neocare.api.domain.model.Alerta;
import com.neocare.api.infrastructure.entity.JpaAlertaEntity;
import com.neocare.api.infrastructure.entity.JpaMedicaoEntity;
import com.neocare.api.infrastructure.entity.JpaUsuarioEntity;
import com.neocare.api.interfaces.dto.output.AlertaOutDto;

public final class AlertaMapper {

    private AlertaMapper() {
    }

    public static AlertaOutDto toOutDto(Alerta alerta) {
        return new AlertaOutDto(
                alerta.getId(),
                alerta.getUsuarioId(),
                alerta.getMedicaoId(),
                alerta.getTipoAlerta(),
                alerta.getValorDetectado(),
                alerta.getSeveridade(),
                alerta.getMensagem(),
                alerta.getDataNotificacao()
        );
    }

    public static JpaAlertaEntity toEntity(Alerta alerta, JpaUsuarioEntity usuarioEntity, JpaMedicaoEntity medicaoEntity) {
        return new JpaAlertaEntity(
                usuarioEntity,
                medicaoEntity,
                alerta.getTipoAlerta(),
                alerta.getValorDetectado(),
                alerta.getSeveridade(),
                alerta.getMensagem(),
                alerta.getDataNotificacao()
        );
    }

    public static Alerta toDomain(JpaAlertaEntity entity) {
        return new Alerta(
                entity.getId(),
                entity.getUsuarioEntity().getId(),
                entity.getMedicaoEntity().getId(),
                entity.getTipoAlerta(),
                entity.getValorDetectado(),
                entity.getSeveridade(),
                entity.getMensagem(),
                entity.getDataNotificacao()
        );
    }
}
