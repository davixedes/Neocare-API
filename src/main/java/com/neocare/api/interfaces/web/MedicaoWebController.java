package com.neocare.api.interfaces.web;

import com.neocare.api.application.usecase.alerta.GerarAlertaPorPredicaoUseCase;
import com.neocare.api.application.usecase.medicao.estresse.ListarMedicoesEstresseUseCase;
import com.neocare.api.application.usecase.medicao.estresse.RegistrarMedicaoEstresseUseCase;
import com.neocare.api.application.usecase.medicao.vital.ListarMedicoesVitaisUseCase;
import com.neocare.api.application.usecase.medicao.vital.RegistrarMedicaoVitalUseCase;
import com.neocare.api.application.usecase.predicao.AnalisarMedicaoUseCase;
import com.neocare.api.application.usecase.predicao.BuscarPredicoesPorMedicaoIdsUseCase;
import com.neocare.api.application.usecase.usuario.LocalizarUsuarioPorUsernameUseCase;
import com.neocare.api.domain.enums.TipoAlerta;
import com.neocare.api.domain.model.MedicaoEstresse;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.domain.model.Usuario;
import com.neocare.api.interfaces.dto.form.MedicaoEstresseForm;
import com.neocare.api.interfaces.dto.form.MedicaoVitalForm;
import com.neocare.api.interfaces.mapper.MedicaoEstresseMapper;
import com.neocare.api.interfaces.mapper.MedicaoVitalMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/medicoes-web")
public class MedicaoWebController {

    private static final Logger log = LoggerFactory.getLogger(MedicaoWebController.class);

    private final ListarMedicoesEstresseUseCase listarMedicoesEstresse;
    private final ListarMedicoesVitaisUseCase listarMedicoesVitais;
    private final RegistrarMedicaoEstresseUseCase registrarMedicaoEstresse;
    private final RegistrarMedicaoVitalUseCase registrarMedicaoVital;
    private final LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername;
    private final AnalisarMedicaoUseCase analisarMedicaoUseCase;
    private final GerarAlertaPorPredicaoUseCase gerarAlertaPorPredicaoUseCase;
    private final BuscarPredicoesPorMedicaoIdsUseCase buscarPredicoesPorMedicaoIds;

    public MedicaoWebController(ListarMedicoesEstresseUseCase listarMedicoesEstresse,
                                ListarMedicoesVitaisUseCase listarMedicoesVitais,
                                RegistrarMedicaoEstresseUseCase registrarMedicaoEstresse,
                                RegistrarMedicaoVitalUseCase registrarMedicaoVital,
                                LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername,
                                AnalisarMedicaoUseCase analisarMedicaoUseCase,
                                GerarAlertaPorPredicaoUseCase gerarAlertaPorPredicaoUseCase,
                                BuscarPredicoesPorMedicaoIdsUseCase buscarPredicoesPorMedicaoIds) {
        this.listarMedicoesEstresse = listarMedicoesEstresse;
        this.listarMedicoesVitais = listarMedicoesVitais;
        this.registrarMedicaoEstresse = registrarMedicaoEstresse;
        this.registrarMedicaoVital = registrarMedicaoVital;
        this.localizarUsuarioPorUsername = localizarUsuarioPorUsername;
        this.analisarMedicaoUseCase = analisarMedicaoUseCase;
        this.gerarAlertaPorPredicaoUseCase = gerarAlertaPorPredicaoUseCase;
        this.buscarPredicoesPorMedicaoIds = buscarPredicoesPorMedicaoIds;
    }

    @GetMapping
    public String listarMedicoes(Authentication authentication, Model model) {
        Usuario usuario = localizarUsuarioPorUsername.execute(authentication.getName());
        Long usuarioId = usuario.getId();

        List<MedicaoEstresse> medicoesEstresse = listarMedicoesEstresse.porUsuario(usuarioId);
        List<MedicaoVital> medicoesVitais = listarMedicoesVitais.porUsuario(usuarioId);

        model.addAttribute("usuario", usuario);
        model.addAttribute("medicoesEstresse", medicoesEstresse);
        model.addAttribute("medicoesVitais", medicoesVitais);
        model.addAttribute("predicoesEstresse",
                buscarPredicoesPorMedicaoIds.execute(medicoesEstresse.stream().map(MedicaoEstresse::getId).toList()));
        model.addAttribute("predicoesVitais",
                buscarPredicoesPorMedicaoIds.execute(medicoesVitais.stream().map(MedicaoVital::getId).toList()));
        return "medicao/lista";
    }

    @GetMapping("/nova-estresse")
    public String formEstresse(Model model) {
        model.addAttribute("medicaoEstresseForm", new MedicaoEstresseForm());
        return "medicao/form-estresse";
    }

    @PostMapping("/nova-estresse")
    public String registrarEstresse(@Valid @ModelAttribute("medicaoEstresseForm") MedicaoEstresseForm form,
                                    BindingResult result,
                                    Authentication authentication,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "medicao/form-estresse";
        }

        try {
            Usuario usuario = localizarUsuarioPorUsername.execute(authentication.getName());
            MedicaoEstresse salva = registrarMedicaoEstresse.execute(MedicaoEstresseMapper.fromForm(form, usuario.getId()));
            ResultadoPredicao resultado = analisarMedicaoUseCase.executarParaEstresse(salva).orElse(null);
            if (resultado != null) {
                gerarAlertaPorPredicaoUseCase.execute(resultado, salva.getIdUsuario(), TipoAlerta.ESTRESSE);
            }
            redirectAttributes.addFlashAttribute("medicaoEstresse", salva);
            redirectAttributes.addFlashAttribute("resultadoPredicao", resultado);
            redirectAttributes.addFlashAttribute("tipoMedicao", "ESTRESSE");
            return "redirect:/medicoes-web/resultado";
        } catch (Exception e) {
            log.warn("Erro ao registrar medição de estresse: {}", e.getMessage());
            model.addAttribute("erro", "Não foi possível registrar a medição de estresse. Verifique os dados e tente novamente.");
            return "medicao/form-estresse";
        }
    }

    @GetMapping("/nova-vital")
    public String formVital(Model model) {
        model.addAttribute("medicaoVitalForm", new MedicaoVitalForm());
        return "medicao/form-vital";
    }

    @PostMapping("/nova-vital")
    public String registrarVital(@Valid @ModelAttribute("medicaoVitalForm") MedicaoVitalForm form,
                                 BindingResult result,
                                 Authentication authentication,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "medicao/form-vital";
        }

        try {
            Usuario usuario = localizarUsuarioPorUsername.execute(authentication.getName());
            MedicaoVital salva = registrarMedicaoVital.execute(MedicaoVitalMapper.fromForm(form, usuario.getId()));
            ResultadoPredicao resultado = analisarMedicaoUseCase.executarParaVital(salva).orElse(null);
            if (resultado != null) {
                gerarAlertaPorPredicaoUseCase.execute(resultado, salva.getIdUsuario(), TipoAlerta.AVC);
            }
            redirectAttributes.addFlashAttribute("medicaoVital", salva);
            redirectAttributes.addFlashAttribute("resultadoPredicao", resultado);
            redirectAttributes.addFlashAttribute("tipoMedicao", "VITAL");
            return "redirect:/medicoes-web/resultado";
        } catch (Exception e) {
            log.warn("Erro ao registrar medição vital: {}", e.getMessage());
            model.addAttribute("erro", "Não foi possível registrar a medição vital. Verifique os dados e tente novamente.");
            return "medicao/form-vital";
        }
    }

    @GetMapping("/resultado")
    public String resultadoPredicao(Model model) {
        if (!model.containsAttribute("tipoMedicao")) {
            return "redirect:/medicoes-web";
        }
        return "medicao/resultado";
    }
}
