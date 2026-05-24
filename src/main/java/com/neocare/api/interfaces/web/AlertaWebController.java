package com.neocare.api.interfaces.web;

import com.neocare.api.application.usecase.alerta.ListarAlertasUseCase;
import com.neocare.api.application.usecase.alerta.MarcarAlertaComoLidoUseCase;
import com.neocare.api.application.usecase.usuario.LocalizarUsuarioPorUsernameUseCase;
import com.neocare.api.domain.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alertas-web")
public class AlertaWebController {

    private static final Logger log = LoggerFactory.getLogger(AlertaWebController.class);

    private final ListarAlertasUseCase listarAlertasUseCase;
    private final MarcarAlertaComoLidoUseCase marcarAlertaComoLidoUseCase;
    private final LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername;

    public AlertaWebController(ListarAlertasUseCase listarAlertasUseCase,
                               MarcarAlertaComoLidoUseCase marcarAlertaComoLidoUseCase,
                               LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername) {
        this.listarAlertasUseCase = listarAlertasUseCase;
        this.marcarAlertaComoLidoUseCase = marcarAlertaComoLidoUseCase;
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

    @PostMapping("/{id}/marcar-lido")
    public String marcarComoLido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            marcarAlertaComoLidoUseCase.execute(id);
            redirectAttributes.addFlashAttribute("sucesso", "Alerta marcado como lido.");
        } catch (Exception e) {
            log.warn("Erro ao marcar alerta {} como lido: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("erro", "Não foi possível marcar o alerta como lido.");
        }
        return "redirect:/alertas-web";
    }
}
