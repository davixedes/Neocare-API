package com.neocare.api.interfaces.mapper;

import com.neocare.api.domain.model.Dispositivo;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.infrastructure.entity.JpaDispositivoEntity;
import com.neocare.api.infrastructure.entity.JpaMedicaoVitalEntity;
import com.neocare.api.infrastructure.entity.JpaUsuarioEntity;
import com.neocare.api.domain.enums.TipoMedicao;
import com.neocare.api.interfaces.dto.form.MedicaoVitalForm;
import com.neocare.api.interfaces.dto.input.MedicaoVitalInDto;
import com.neocare.api.interfaces.dto.output.DispositivoMedicaoOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoVitalOutDto;
import com.neocare.api.interfaces.dto.output.ResultadoPredicaoOutDto;

public final class MedicaoVitalMapper {

    public static MedicaoVital fromForm(MedicaoVitalForm form, Long usuarioId) {
        return new MedicaoVital(
                usuarioId,
                form.getIdDispositivo(),
                TipoMedicao.MEDICAO_VITAL,
                form.getBatimentosPorMinuto(),
                form.getOxigenacaoSangue(),
                form.getPressaoSistolica(),
                form.getPressaoDiastolica()
        );
    }

    public static MedicaoVital toModel(MedicaoVitalInDto medicaoVitalInDto) {
        return new MedicaoVital(
                medicaoVitalInDto.idUsuario(),
                medicaoVitalInDto.idDispositivo(),
                medicaoVitalInDto.tipoMedicao(),
                medicaoVitalInDto.batimentosPorMinuto(),
                medicaoVitalInDto.oxigenacaoSangue(),
                medicaoVitalInDto.pressaoSistolica(),
                medicaoVitalInDto.pressaoDiastolica()
        );
    }

    public static MedicaoVitalOutDto toOutDto(MedicaoVital registeredMedicaoVital, String nomeUsuario,
                                               Dispositivo dispositivo, Long idDispositivo,
                                               ResultadoPredicaoOutDto resultadoPredicao) {
        DispositivoMedicaoOutDto dispositivoMedicaoOutDto = new DispositivoMedicaoOutDto(
                idDispositivo,
                dispositivo.getTipoDispositivo(),
                dispositivo.getEnderecoDisp(),
                dispositivo.getAtivo()
        );

        MedicaoOutDto medicaoOutDto = new MedicaoOutDto(
                registeredMedicaoVital.getId(),
                nomeUsuario,
                dispositivoMedicaoOutDto,
                registeredMedicaoVital.getDataMedicao(),
                registeredMedicaoVital.getTipoMedicao()
        );

        return new MedicaoVitalOutDto(
                medicaoOutDto,
                registeredMedicaoVital.getBatimentosPorMinuto(),
                registeredMedicaoVital.getOxigenacaoSangue(),
                registeredMedicaoVital.getPressaoSistolica(),
                registeredMedicaoVital.getPressaoDiastolica(),
                dispositivoMedicaoOutDto,
                resultadoPredicao
        );
    }

    public static JpaMedicaoVitalEntity toEntity(MedicaoVital medicaoVital, JpaUsuarioEntity usuarioEntity, JpaDispositivoEntity dispositivoEntity) {
        return new JpaMedicaoVitalEntity(
                usuarioEntity,
                dispositivoEntity,
                medicaoVital.getDataMedicao(),
                medicaoVital.getTipoMedicao(),
                medicaoVital.getBatimentosPorMinuto(),
                medicaoVital.getOxigenacaoSangue(),
                medicaoVital.getPressaoSistolica(),
                medicaoVital.getPressaoDiastolica()
        );
    }

    public static MedicaoVital jpaToDomain(JpaMedicaoVitalEntity savedEntity) {
        return new MedicaoVital(
                savedEntity.getId(),
                savedEntity.getJpaUsuarioEntity().getId(),
                savedEntity.getJpaDispositivoEntity().getId(),
                savedEntity.getDataMedicao(),
                savedEntity.getTipoMedicao(),
                savedEntity.getBatimentosPorMinuto(),
                savedEntity.getOxigenacaoSangue(),
                savedEntity.getPressaoSistolica(),
                savedEntity.getPressaoDiastolica()
        );
    }
}
