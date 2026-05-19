package com.neocare.api.interfaces.web;

import com.neocare.api.application.usecase.usuario.CriarUsuarioUseCase;
import com.neocare.api.interfaces.dto.form.RegistroForm;
import com.neocare.api.interfaces.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthWebController {

    private static final Logger log = LoggerFactory.getLogger(AuthWebController.class);

    private final CriarUsuarioUseCase criarUsuarioUseCase;

    public AuthWebController(CriarUsuarioUseCase criarUsuarioUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String exibirFormRegistro(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registroForm") RegistroForm form,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/registro";
        }

        try {
            criarUsuarioUseCase.execute(UsuarioMapper.fromRegistroForm(form));
            redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado com sucesso! Faça login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            log.warn("Erro ao registrar usuário: {}", e.getMessage());
            model.addAttribute("erro", e.getMessage());
            return "auth/registro";
        }
    }
}
