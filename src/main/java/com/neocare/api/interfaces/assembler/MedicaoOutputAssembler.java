package com.neocare.api.interfaces.assembler;

import com.neocare.api.application.usecase.dispositivo.LocalizarDispositivoUseCase;
import com.neocare.api.application.usecase.usuario.LocalizarUsuarioPorIdUseCase;
import com.neocare.api.domain.model.Dispositivo;
import com.neocare.api.domain.model.MedicaoEstresse;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.Usuario;
import com.neocare.api.interfaces.dto.output.MedicaoEstresseOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoVitalOutDto;
import com.neocare.api.interfaces.mapper.MedicaoEstresseMapper;
import com.neocare.api.interfaces.mapper.MedicaoVitalMapper;

public final class MedicaoOutputAssembler {

    private final LocalizarUsuarioPorIdUseCase localizarUsuarioPorId;
    private final LocalizarDispositivoUseCase localizarDispositivo;

    public MedicaoOutputAssembler(LocalizarUsuarioPorIdUseCase localizarUsuarioPorId,
                                   LocalizarDispositivoUseCase localizarDispositivo) {
        this.localizarUsuarioPorId = localizarUsuarioPorId;
        this.localizarDispositivo = localizarDispositivo;
    }

    public MedicaoEstresseOutDto toEstresseOutDto(MedicaoEstresse medicao) {
        Usuario usuario = localizarUsuarioPorId.execute(medicao.getIdUsuario());
        Dispositivo dispositivo = localizarDispositivo.execute(medicao.getIdDispositivo());
        return MedicaoEstresseMapper.toOutDto(medicao, usuario.getNome(), dispositivo, medicao.getIdDispositivo());
    }

    public MedicaoVitalOutDto toVitalOutDto(MedicaoVital medicao) {
        Usuario usuario = localizarUsuarioPorId.execute(medicao.getIdUsuario());
        Dispositivo dispositivo = localizarDispositivo.execute(medicao.getIdDispositivo());
        return MedicaoVitalMapper.toOutDto(medicao, usuario.getNome(), dispositivo, medicao.getIdDispositivo());
    }
}
