package br.com.gabriela.safra_control.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/painel")
public class RelatorioPaginaController {

    // O relatorio-api.js consulta os valores por GET REST.
    @GetMapping("/safras/{safraId}/relatorio")
    public String relatorioDaSafra(@PathVariable Integer safraId, Model model) {
        model.addAttribute("safraId", safraId);
        model.addAttribute("maisLucrativa", false);
        return "relatorio";
    }

    // Abre a mesma JSP para a safra mais lucrativa.
    @GetMapping("/mais-lucrativa")
    public String safraMaisLucrativa(Model model) {
        model.addAttribute("maisLucrativa", true);
        return "relatorio";
    }
}
