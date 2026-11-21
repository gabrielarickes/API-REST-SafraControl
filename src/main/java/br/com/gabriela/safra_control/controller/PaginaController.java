
package br.com.gabriela.safra_control.controller;

import br.com.gabriela.safra_control.dto.SafraDto;
import br.com.gabriela.safra_control.service.SafraService;
import br.com.gabriela.safra_control.database.model.SafraEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PaginaController {

    private final SafraService safraService;

    // GET mostra as safras cadastradas
    @GetMapping("/painel")
    public String abrirPainel() {
        return "inicio";
    }

    // GET abre o formulário de cadastro
    @GetMapping("/painel/novo")
    public String abrirFormulario(Model model) {

        model.addAttribute("safraDto", new SafraDto());

        return "nova-safra";
    }

    @GetMapping("/")
    public String paginaInicial() {
        return "redirect:/painel";
    }

    //GET abre a página de edição
    @GetMapping("/painel/editar/{id}")
    public String abrirEdicao(
            @PathVariable Integer id,
            Model model) {

        SafraEntity safra = safraService.findBySafra(id);

        SafraDto dto = new SafraDto();

        dto.setAno(safra.getAno());
        dto.setTipoCultura(safra.getTipoCultura());
        dto.setQuantidade(safra.getQuantidade());
        dto.setUnidadeMedida(safra.getUnidadeMedida());
        dto.setValorVendaUnidade(safra.getValorVendaUnidade());

        model.addAttribute("safraDto", dto);
        model.addAttribute("safraId", id);

        return "editar-safra";
    }
}
