package com.neocare.api.interfaces.controller;

import com.neocare.api.interfaces.dto.input.MedicaoPsicofisiologicaInDto;
import com.neocare.api.interfaces.dto.input.MedicaoVitalInDto;
import com.neocare.api.interfaces.dto.output.MedicaoPsicofisiologicaOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoVitalOutDto;

public interface MedicaoController {

    MedicaoPsicofisiologicaOutDto registrarMedicaoPsicofisiologica(MedicaoPsicofisiologicaInDto inDto);

    MedicaoVitalOutDto registrarMedicaoVital(MedicaoVitalInDto medicaoVitalInDto);
}
