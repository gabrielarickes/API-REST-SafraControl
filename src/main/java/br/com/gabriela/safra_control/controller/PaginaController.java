package br.com.gabriela.safra_control.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Este controller só abre as páginas JSP.
// Todos os dados são carregados pelo navegador, via API REST (fetch).
@Controller
public class PaginaController {

    @GetMapping("/")
    public String paginaInicial() {
        return "redirect:/painel";
    }

    // Lista de safras (o safras-api.js consulta GET /safra)
    @GetMapping("/painel")
    public String abrirPainel() {
        return "inicio";
    }

    // Formulário de cadastro (o cadastrar-safra-api.js envia POST /safra)
    @GetMapping("/painel/novo")
    public String abrirFormulario() {
        return "nova-safra";
    }

    // Formulário de edição (o editar-safra-api.js consulta GET /safra/{id} e envia PUT)
    @GetMapping("/painel/editar/{id}")
    public String abrirEdicao(@PathVariable Integer id, Model model) {
        model.addAttribute("safraId", id);
        return "editar-safra";
    }
}
