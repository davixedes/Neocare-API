package com.neocare.api.interfaces.web;

import com.neocare.api.application.usecase.alerta.GerarAlertaPorPredicaoUseCase;
import com.neocare.api.application.usecase.medicao.psicofisiologica.ListarMedicoesPsicofisiologicasUseCase;
import com.neocare.api.application.usecase.medicao.psicofisiologica.RegistrarMedicaoPsicofisiologicaUseCase;
import com.neocare.api.application.usecase.medicao.vital.ListarMedicoesVitaisUseCase;
import com.neocare.api.application.usecase.medicao.vital.RegistrarMedicaoVitalUseCase;
import com.neocare.api.application.usecase.predicao.AnalisarMedicaoUseCase;
import com.neocare.api.application.usecase.predicao.BuscarPredicoesPorMedicaoIdsUseCase;
import com.neocare.api.application.usecase.usuario.LocalizarUsuarioPorUsernameUseCase;
import com.neocare.api.domain.enums.TipoAlerta;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.domain.model.Usuario;
import com.neocare.api.interfaces.dto.form.MedicaoPsicofisiologicaForm;
import com.neocare.api.interfaces.dto.form.MedicaoVitalForm;
import com.neocare.api.interfaces.mapper.MedicaoPsicofisiologicaMapper;
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

    private final ListarMedicoesPsicofisiologicasUseCase listarMedicoesPsicofisiologicas;
    private final ListarMedicoesVitaisUseCase listarMedicoesVitais;
    private final RegistrarMedicaoPsicofisiologicaUseCase registrarMedicaoPsicofisiologica;
    private final RegistrarMedicaoVitalUseCase registrarMedicaoVital;
    private final LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername;
    private final AnalisarMedicaoUseCase analisarMedicaoUseCase;
    private final GerarAlertaPorPredicaoUseCase gerarAlertaPorPredicaoUseCase;
    private final BuscarPredicoesPorMedicaoIdsUseCase buscarPredicoesPorMedicaoIds;

    public MedicaoWebController(ListarMedicoesPsicofisiologicasUseCase listarMedicoesPsicofisiologicas,
                                ListarMedicoesVitaisUseCase listarMedicoesVitais,
                                RegistrarMedicaoPsicofisiologicaUseCase registrarMedicaoPsicofisiologica,
                                RegistrarMedicaoVitalUseCase registrarMedicaoVital,
                                LocalizarUsuarioPorUsernameUseCase localizarUsuarioPorUsername,
                                AnalisarMedicaoUseCase analisarMedicaoUseCase,
                                GerarAlertaPorPredicaoUseCase gerarAlertaPorPredicaoUseCase,
                                BuscarPredicoesPorMedicaoIdsUseCase buscarPredicoesPorMedicaoIds) {
        this.listarMedicoesPsicofisiologicas = listarMedicoesPsicofisiologicas;
        this.listarMedicoesVitais = listarMedicoesVitais;
        this.registrarMedicaoPsicofisiologica = registrarMedicaoPsicofisiologica;
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

        List<MedicaoPsicofisiologica> medicoesPsicofisiologicas = listarMedicoesPsicofisiologicas.porUsuario(usuarioId);
        List<MedicaoVital> medicoesVitais = listarMedicoesVitais.porUsuario(usuarioId);

        model.addAttribute("usuario", usuario);
        model.addAttribute("medicoesPsicofisiologicas", medicoesPsicofisiologicas);
        model.addAttribute("medicoesVitais", medicoesVitais);
        model.addAttribute("predicoesPsicofisiologicas",
                buscarPredicoesPorMedicaoIds.execute(medicoesPsicofisiologicas.stream().map(MedicaoPsicofisiologica::getId).toList()));
        model.addAttribute("predicoesVitais",
                buscarPredicoesPorMedicaoIds.execute(medicoesVitais.stream().map(MedicaoVital::getId).toList()));
        return "medicao/lista";
    }

    @GetMapping("/nova-psicofisiologica")
    public String formPsicofisiologica(Model model) {
        model.addAttribute("medicaoPsicofisiologicaForm", new MedicaoPsicofisiologicaForm());
        return "medicao/form-psicofisiologica";
    }

    @PostMapping("/nova-psicofisiologica")
    public String registrarPsicofisiologica(@Valid @ModelAttribute("medicaoPsicofisiologicaForm") MedicaoPsicofisiologicaForm form,
                                            BindingResult result,
                                            Authentication authentication,
                                            Model model,
                                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "medicao/form-psicofisiologica";
        }

        try {
            Usuario usuario = localizarUsuarioPorUsername.execute(authentication.getName());
            MedicaoPsicofisiologica salva = registrarMedicaoPsicofisiologica.execute(MedicaoPsicofisiologicaMapper.fromForm(form, usuario.getId()));
            ResultadoPredicao resultado = analisarMedicaoUseCase.executarParaPsicofisiologica(salva).orElse(null);
            if (resultado != null) {
                gerarAlertaPorPredicaoUseCase.execute(resultado, salva.getIdUsuario(), TipoAlerta.ESTRESSE);
            }
            redirectAttributes.addFlashAttribute("medicaoPsicofisiologica", salva);
            redirectAttributes.addFlashAttribute("resultadoPredicao", resultado);
            redirectAttributes.addFlashAttribute("tipoMedicao", "PSICOFISIOLOGICA");
            return "redirect:/medicoes-web/resultado";
        } catch (Exception e) {
            log.warn("Erro ao registrar medição psicofisiológica: {}", e.getMessage());
            model.addAttribute("erro", "Não foi possível registrar a medição psicofisiológica. Verifique os dados e tente novamente.");
            return "medicao/form-psicofisiologica";
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
