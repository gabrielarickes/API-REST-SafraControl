package br.com.gabriela.safra_control.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

// Este controller só abre as páginas JSP.
// Todos os dados são carregados pelo navegador, via API REST (fetch).
@Controller
@RequestMapping("/painel/safras/{safraId}/gastos")
public class GastosPaginaController {

    // Lista de gastos (o gastos-api.js consulta GET /safra/{id}/gastos)
    @GetMapping
    public String listarGastos(@PathVariable Integer safraId, Model model) {
        model.addAttribute("safraId", safraId);
        return "gastos";
    }

    // Formulário de cadastro (o formulario-gastos.js envia POST /safra/{id}/gastos)
    @GetMapping("/novo")
    public String abrirCadastro(@PathVariable Integer safraId, Model model) {
        model.addAttribute("safraId", safraId);
        return "formulario-gastos";
    }

    // Formulário de edição (o formulario-gastos.js consulta a API e envia PUT /gastos/{id})
    @GetMapping("/editar/{gastoId}")
    public String abrirEdicao(
            @PathVariable Integer safraId,
            @PathVariable Integer gastoId,
            Model model) {
        model.addAttribute("safraId", safraId);
        model.addAttribute("gastoId", gastoId);
        return "formulario-gastos";
    }
}
