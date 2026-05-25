package com.neocare.api.interfaces.assembler;

import com.neocare.api.application.usecase.dispositivo.LocalizarDispositivoUseCase;
import com.neocare.api.application.usecase.usuario.LocalizarUsuarioPorIdUseCase;
import com.neocare.api.domain.model.Dispositivo;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.domain.model.Usuario;
import com.neocare.api.interfaces.dto.output.MedicaoPsicofisiologicaOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoVitalOutDto;
import com.neocare.api.interfaces.dto.output.ResultadoPredicaoOutDto;
import com.neocare.api.interfaces.mapper.MedicaoPsicofisiologicaMapper;
import com.neocare.api.interfaces.mapper.MedicaoVitalMapper;

public final class MedicaoOutputAssembler {

    private final LocalizarUsuarioPorIdUseCase localizarUsuarioPorId;
    private final LocalizarDispositivoUseCase localizarDispositivo;

    public MedicaoOutputAssembler(LocalizarUsuarioPorIdUseCase localizarUsuarioPorId,
                                   LocalizarDispositivoUseCase localizarDispositivo) {
        this.localizarUsuarioPorId = localizarUsuarioPorId;
        this.localizarDispositivo = localizarDispositivo;
    }

    public MedicaoPsicofisiologicaOutDto toPsicofisiologicaOutDto(MedicaoPsicofisiologica medicao, ResultadoPredicao resultadoPredicao) {
        Usuario usuario = localizarUsuarioPorId.execute(medicao.getIdUsuario());
        Dispositivo dispositivo = localizarDispositivo.execute(medicao.getIdDispositivo());
        ResultadoPredicaoOutDto resultadoPredicaoOutDto = resultadoPredicao != null
                ? new ResultadoPredicaoOutDto(resultadoPredicao.getScore(), resultadoPredicao.getPredicao(), resultadoPredicao.getAnalisadoEm())
                : null;
        return MedicaoPsicofisiologicaMapper.toOutDto(medicao, usuario.getNome(), dispositivo, medicao.getIdDispositivo(), resultadoPredicaoOutDto);
    }

    public MedicaoVitalOutDto toVitalOutDto(MedicaoVital medicao, ResultadoPredicao resultadoPredicao) {
        Usuario usuario = localizarUsuarioPorId.execute(medicao.getIdUsuario());
        Dispositivo dispositivo = localizarDispositivo.execute(medicao.getIdDispositivo());
        ResultadoPredicaoOutDto resultadoPredicaoOutDto = resultadoPredicao != null
                ? new ResultadoPredicaoOutDto(resultadoPredicao.getScore(), resultadoPredicao.getPredicao(), resultadoPredicao.getAnalisadoEm())
                : null;
        return MedicaoVitalMapper.toOutDto(medicao, usuario.getNome(), dispositivo, medicao.getIdDispositivo(), resultadoPredicaoOutDto);
    }
}
