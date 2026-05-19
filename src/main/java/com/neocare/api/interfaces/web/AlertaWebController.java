package com.neocare.api.interfaces.web;

import com.neocare.api.application.usecase.alerta.ListarAlertasUseCase;
import com.neocare.api.application.usecase.usuario.LocalizarUsuarioPorUsernameUseCase;
import com.neocare.api.domain.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alertas-web")
public class AlertaWebController {

    private final ListarAlertasUseCase listarAlertasUseCase;
    private final LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername;

    public AlertaWebController(ListarAlertasUseCase listarAlertasUseCase,
                               LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername) {
        this.listarAlertasUseCase = listarAlertasUseCase;
        this.localizarUsuarioPorUsername = localizarUsuarioPorUsername;
    }

    @GetMapping
    public String listarAlertas(Authentication authentication, Model model) {
        boolean isAdmin = SecurityContextHelper.isAdmin(authentication);

        if (isAdmin) {
            model.addAttribute("alertas", listarAlertasUseCase.todos());
            model.addAttribute("isAdmin", true);
        } else {
            Usuario usuario = localizarUsuarioPorUsername.execute(authentication.getName());
            model.addAttribute("alertas", listarAlertasUseCase.porUsuario(usuario.getId()));
            model.addAttribute("usuario", usuario);
            model.addAttribute("isAdmin", false);
        }

        return "alerta/lista";
    }
}
