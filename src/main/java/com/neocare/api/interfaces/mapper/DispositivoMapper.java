package com.neocare.api.interfaces.mapper;

import com.neocare.api.domain.model.Dispositivo;
import com.neocare.api.infrastructure.entity.JpaDispositivoEntity;
import com.neocare.api.interfaces.dto.output.DispositivoOutDto;

public final class DispositivoMapper {

    private DispositivoMapper() {
    }

    public static DispositivoOutDto toOutDto(Long id, Dispositivo dispositivo) {
        return new DispositivoOutDto(
                id,
                dispositivo.getUsuarioId(),
                dispositivo.getTipoDispositivo(),
                dispositivo.getEnderecoDisp(),
                dispositivo.getAtivo()
        );
    }

    public static Dispositivo jpaEntityToDomain(JpaDispositivoEntity entity) {
        Dispositivo dispositivo = new Dispositivo(
                entity.getUsuarioEntity().getId(),
                entity.getTipoDispositivo(),
                entity.getEnderecoDisp(),
                entity.getAtivo()
        );
        dispositivo.setId(entity.getId());
        return dispositivo;
    }
}
