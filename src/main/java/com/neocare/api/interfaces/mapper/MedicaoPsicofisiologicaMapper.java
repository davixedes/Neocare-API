package com.neocare.api.interfaces.mapper;

import com.neocare.api.domain.enums.TipoMedicao;
import com.neocare.api.domain.model.Dispositivo;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.infrastructure.entity.JpaDispositivoEntity;
import com.neocare.api.infrastructure.entity.JpaMedicaoPsicofisiologicaEntity;
import com.neocare.api.infrastructure.entity.JpaUsuarioEntity;
import com.neocare.api.interfaces.dto.form.MedicaoPsicofisiologicaForm;
import com.neocare.api.interfaces.dto.input.MedicaoPsicofisiologicaInDto;
import com.neocare.api.interfaces.dto.output.DispositivoMedicaoOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoPsicofisiologicaOutDto;
import com.neocare.api.interfaces.dto.output.ResultadoPredicaoOutDto;

public final class MedicaoPsicofisiologicaMapper {

    public static MedicaoPsicofisiologica fromForm(MedicaoPsicofisiologicaForm form, Long usuarioId) {
        return new MedicaoPsicofisiologica(
                usuarioId,
                form.getIdDispositivo(),
                TipoMedicao.MEDICAO_PSICOFISIOLOGICA,
                form.getVariacaoFrequenciaCardiaca(),
                form.getCondutividadePele()
        );
    }

    public static MedicaoPsicofisiologica toModel(MedicaoPsicofisiologicaInDto inDto) {
        return new MedicaoPsicofisiologica(
                inDto.idUsuario(),
                inDto.idDispositivo(),
                inDto.tipoMedicao(),
                inDto.variacaoFrequenciaCardiaca(),
                inDto.condutividadePele()
        );
    }

    public static MedicaoPsicofisiologicaOutDto toOutDto(MedicaoPsicofisiologica registrada, String nomeUsuario,
                                                          Dispositivo dispositivo, Long idDispositivo,
                                                          ResultadoPredicaoOutDto resultadoPredicao) {
        DispositivoMedicaoOutDto dispositivoMedicaoOutDto = new DispositivoMedicaoOutDto(
                idDispositivo,
                dispositivo.getTipoDispositivo(),
                dispositivo.getEnderecoDisp(),
                dispositivo.getAtivo()
        );

        MedicaoOutDto medicaoOutDto = new MedicaoOutDto(
                registrada.getId(),
                nomeUsuario,
                dispositivoMedicaoOutDto,
                registrada.getDataMedicao(),
                registrada.getTipoMedicao()
        );

        return new MedicaoPsicofisiologicaOutDto(
                registrada.getVariacaoFrequenciaCardiaca(),
                registrada.getCondutividadePele(),
                medicaoOutDto,
                resultadoPredicao
        );
    }

    public static JpaMedicaoPsicofisiologicaEntity toEntity(MedicaoPsicofisiologica medicao, JpaUsuarioEntity usuarioEntity, JpaDispositivoEntity dispositivoEntity) {
        return new JpaMedicaoPsicofisiologicaEntity(
                usuarioEntity,
                dispositivoEntity,
                medicao.getDataMedicao(),
                medicao.getTipoMedicao(),
                medicao.getVariacaoFrequenciaCardiaca(),
                medicao.getCondutividadePele()
        );
    }

    public static MedicaoPsicofisiologica jpaToDomain(JpaMedicaoPsicofisiologicaEntity entity) {
        return new MedicaoPsicofisiologica(
                entity.getId(),
                entity.getJpaUsuarioEntity().getId(),
                entity.getJpaDispositivoEntity().getId(),
                entity.getDataMedicao(),
                entity.getTipoMedicao(),
                entity.getVariacaoFrequenciaCardiaca(),
                entity.getCondutividadePele()
        );
    }
}
