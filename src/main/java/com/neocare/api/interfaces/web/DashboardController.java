package com.neocare.api.interfaces.web;

import com.neocare.api.application.usecase.alerta.ListarAlertasUseCase;
import com.neocare.api.application.usecase.medicao.estresse.ListarMedicoesEstresseUseCase;
import com.neocare.api.application.usecase.medicao.vital.ListarMedicoesVitaisUseCase;
import com.neocare.api.application.usecase.usuario.LocalizarTodosOsUsuariosUseCase;
import com.neocare.api.application.usecase.usuario.LocalizarUsuarioPorUsernameUseCase;
import com.neocare.api.domain.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername;
    private final ListarMedicoesEstresseUseCase listarMedicoesEstresse;
    private final ListarMedicoesVitaisUseCase listarMedicoesVitais;
    private final ListarAlertasUseCase listarAlertas;
    private final LocalizarTodosOsUsuariosUseCase localizarTodosOsUsuarios;

    public DashboardController(LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername,
                               ListarMedicoesEstresseUseCase listarMedicoesEstresse,
                               ListarMedicoesVitaisUseCase listarMedicoesVitais,
                               ListarAlertasUseCase listarAlertas,
                               LocalizarTodosOsUsuariosUseCase localizarTodosOsUsuarios) {
        this.localizarUsuarioPorUsername = localizarUsuarioPorUsername;
        this.listarMedicoesEstresse = listarMedicoesEstresse;
        this.listarMedicoesVitais = listarMedicoesVitais;
        this.listarAlertas = listarAlertas;
        this.localizarTodosOsUsuarios = localizarTodosOsUsuarios;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        boolean isAdmin = SecurityContextHelper.isAdmin(authentication);

        if (isAdmin) {
            model.addAttribute("alertas", listarAlertas.todos());
            model.addAttribute("usuarios", localizarTodosOsUsuarios.execute());
            return "dashboard/admin";
        }

        Usuario usuario = localizarUsuarioPorUsername.execute(authentication.getName());
        Long usuarioId = usuario.getId();

        model.addAttribute("usuario", usuario);
        model.addAttribute("medicoesEstresse", listarMedicoesEstresse.porUsuario(usuarioId));
        model.addAttribute("medicoesVitais", listarMedicoesVitais.porUsuario(usuarioId));
        model.addAttribute("alertas", listarAlertas.porUsuario(usuarioId));
        return "dashboard/user";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}
