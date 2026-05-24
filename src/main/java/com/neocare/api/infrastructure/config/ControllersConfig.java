package com.neocare.api.infrastructure.config;

import com.neocare.api.application.usecase.alerta.GerarAlertaPorPredicaoUseCase;
import com.neocare.api.application.usecase.dispositivo.LocalizarDispositivoUseCase;
import com.neocare.api.application.usecase.predicao.AnalisarMedicaoUseCase;
import com.neocare.api.application.usecase.medicao.estresse.RegistrarMedicaoEstresseUseCase;
import com.neocare.api.application.usecase.medicao.vital.RegistrarMedicaoVitalUseCase;
import com.neocare.api.application.usecase.usuario.*;
import com.neocare.api.infrastructure.security.JwtUtil;
import com.neocare.api.interfaces.assembler.MedicaoOutputAssembler;
import com.neocare.api.interfaces.controller.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class ControllersConfig {

    @Bean
    public UsuarioController usuarioController(
            CriarUsuarioUseCase criarUsuarioUseCase, LocalizarUsuarioUseCase localizarUsuarioUseCase,
            LocalizarTodosOsUsuariosUseCase localizarTodosOsUsuariosUseCase, EditarUsuarioUseCase editarUsuarioUseCase,
            DesativarUsuarioUseCase desativarUsuarioUseCase, LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsernameUseCase
    ){
        return new UsuarioControllerImpl(
                criarUsuarioUseCase, localizarUsuarioUseCase,
                localizarTodosOsUsuariosUseCase, editarUsuarioUseCase,
                desativarUsuarioUseCase, localizarUsuarioPorUsernameUseCase);
    }

    @Bean
    public MedicaoOutputAssembler medicaoOutputAssembler(
            LocalizarUsuarioPorIdUseCase localizarUsuarioPorIdUseCase,
            LocalizarDispositivoUseCase localizarDispositivoUseCase) {
        return new MedicaoOutputAssembler(localizarUsuarioPorIdUseCase, localizarDispositivoUseCase);
    }

    @Bean
    public MedicaoController medicaoController(
            RegistrarMedicaoEstresseUseCase registrarMedicaoEstresseUseCase,
            RegistrarMedicaoVitalUseCase registrarMedicaoVitalUseCase,
            AnalisarMedicaoUseCase analisarMedicaoUseCase,
            GerarAlertaPorPredicaoUseCase gerarAlertaPorPredicaoUseCase,
            MedicaoOutputAssembler medicaoOutputAssembler
    ) {
        return new MedicaoControllerImpl(
                registrarMedicaoEstresseUseCase, registrarMedicaoVitalUseCase,
                analisarMedicaoUseCase, gerarAlertaPorPredicaoUseCase, medicaoOutputAssembler
        );
    }

    @Bean
    public AuthController authController(
            AuthenticationManager authenticationManager, JwtUtil jwtUtil
    ) {
        return new AuthControllerImpl(authenticationManager, jwtUtil);
    }
}
