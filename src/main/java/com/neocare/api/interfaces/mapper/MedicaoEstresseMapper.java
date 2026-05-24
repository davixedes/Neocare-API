package com.neocare.api.interfaces.mapper;

import com.neocare.api.domain.model.Dispositivo;
import com.neocare.api.domain.model.MedicaoEstresse;
import com.neocare.api.infrastructure.entity.JpaDispositivoEntity;
import com.neocare.api.infrastructure.entity.JpaMedicaoEstresseEntity;
import com.neocare.api.infrastructure.entity.JpaUsuarioEntity;
import com.neocare.api.domain.enums.TipoMedicao;
import com.neocare.api.interfaces.dto.form.MedicaoEstresseForm;
import com.neocare.api.interfaces.dto.input.MedicaoEstresseInDto;
import com.neocare.api.interfaces.dto.output.DispositivoMedicaoOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoEstresseOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoOutDto;
import com.neocare.api.interfaces.dto.output.ResultadoPredicaoOutDto;

public final class MedicaoEstresseMapper {

    public static MedicaoEstresse fromForm(MedicaoEstresseForm form, Long usuarioId) {
        return new MedicaoEstresse(
                usuarioId,
                form.getIdDispositivo(),
                TipoMedicao.MEDICAO_ESTRESSE,
                form.getVariacaoFrequenciaCardiaca(),
                form.getCondutividadePele()
        );
    }

    public static MedicaoEstresse toModel(MedicaoEstresseInDto medicaoEstresseInDto) {
        return new MedicaoEstresse(
                medicaoEstresseInDto.idUsuario(),
                medicaoEstresseInDto.idDispositivo(),
                medicaoEstresseInDto.tipoMedicao(),
                medicaoEstresseInDto.variacaoFrequenciaCardiaca(),
                medicaoEstresseInDto.condutividadePele()
        );
    }

    public static MedicaoEstresseOutDto toOutDto(MedicaoEstresse registeredMedicaoEstresse, String nomeUsuario,
                                                  Dispositivo dispositivo, Long idDispositivo,
                                                  ResultadoPredicaoOutDto resultadoPredicao) {
        DispositivoMedicaoOutDto dispositivoMedicaoOutDto = new DispositivoMedicaoOutDto(
                idDispositivo,
                dispositivo.getTipoDispositivo(),
                dispositivo.getEnderecoDisp(),
                dispositivo.getAtivo()
        );

        MedicaoOutDto medicaoOutDto = new MedicaoOutDto(
                registeredMedicaoEstresse.getId(),
                nomeUsuario,
                dispositivoMedicaoOutDto,
                registeredMedicaoEstresse.getDataMedicao(),
                registeredMedicaoEstresse.getTipoMedicao()
        );

        return new MedicaoEstresseOutDto(
                registeredMedicaoEstresse.getVariacaoFrequenciaCardiaca(),
                registeredMedicaoEstresse.getCondutividadePele(),
                medicaoOutDto,
                resultadoPredicao
        );
    }

    public static JpaMedicaoEstresseEntity toEntity(MedicaoEstresse medicaoEstresse, JpaUsuarioEntity jpaUsuarioEntity, JpaDispositivoEntity jpaDispositivoEntity) {
        return new JpaMedicaoEstresseEntity(
                jpaUsuarioEntity,
                jpaDispositivoEntity,
                medicaoEstresse.getDataMedicao(),
                medicaoEstresse.getTipoMedicao(),
                medicaoEstresse.getVariacaoFrequenciaCardiaca(),
                medicaoEstresse.getCondutividadePele()
        );
    }

    public static MedicaoEstresse jpaToDomain(JpaMedicaoEstresseEntity savedEntity) {
        return new MedicaoEstresse(
                savedEntity.getId(),
                savedEntity.getJpaUsuarioEntity().getId(),
                savedEntity.getJpaDispositivoEntity().getId(),
                savedEntity.getDataMedicao(),
                savedEntity.getTipoMedicao(),
                savedEntity.getVariacaoFrequenciaCardiaca(),
                savedEntity.getCondutividadePele()
        );
    }
}
