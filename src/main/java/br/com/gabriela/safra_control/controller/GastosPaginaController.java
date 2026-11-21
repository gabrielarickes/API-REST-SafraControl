
package br.com.gabriela.safra_control.controller;

import br.com.gabriela.safra_control.dto.GastosDto;
import br.com.gabriela.safra_control.dto.GastosResponseDto;
import br.com.gabriela.safra_control.service.GastosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/painel/safras/{safraId}/gastos")
public class GastosPaginaController {

    private final GastosService gastosService;

    // GET mostra gastos de uma safra
    @GetMapping
    public String listarGastos(
            @PathVariable Integer safraId,
            Model model) {

        List<GastosResponseDto> gastos =
                gastosService.findBySafra(safraId);

        BigDecimal totalGastos = BigDecimal.ZERO;

        for (GastosResponseDto gasto : gastos) {
            totalGastos = totalGastos.add(gasto.valor());
        }

        model.addAttribute("safraId", safraId);
        model.addAttribute("gastos", gastos);
        model.addAttribute("totalGastos", totalGastos);

        return "gastos";
    }

    //GET abrir formulário de cadastro
    @GetMapping("/novo")
    public String abrirCadastro(
            @PathVariable Integer safraId,
            Model model) {

        gastosService.findBySafra(safraId);

        model.addAttribute("gastoDto", new GastosDto());

        prepararFormulario(model, safraId, null);

        return "formulario-gastos";
    }

    //GET abrir formulário de edição
    @GetMapping("/editar/{gastoId}")
    public String abrirEdicao(
            @PathVariable Integer safraId,
            @PathVariable Integer gastoId,
            Model model) {

        GastosResponseDto gasto =
                buscarGastoDaSafra(safraId, gastoId);

        GastosDto dto = new GastosDto();

        dto.setTipoGasto(gasto.tipoGasto());
        dto.setDescricao(gasto.descricao());
        dto.setValor(gasto.valor());
        dto.setDataGasto(gasto.dataGasto());

        model.addAttribute("gastoDto", dto);

        prepararFormulario(model, safraId, gastoId);

        return "formulario-gastos";
    }

    // Verifica se o gasto pertence à safra informada
    private GastosResponseDto buscarGastoDaSafra(
            Integer safraId,
            Integer gastoId) {

        List<GastosResponseDto> gastos =
                gastosService.findBySafra(safraId);

        for (GastosResponseDto gasto : gastos) {
            if (gasto.id().equals(gastoId)) {
                return gasto;
            }
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Gasto não encontrado nesta safra"
        );
    }

    // Prepara as informações do formulário JSP
    private void prepararFormulario(
            Model model,
            Integer safraId,
            Integer gastoId) {

        model.addAttribute("safraId", safraId);

        if (gastoId == null) {
            model.addAttribute("titulo", "Cadastrar gasto");
            model.addAttribute(
                    "acao",
                    "/painel/safras/" + safraId + "/gastos/novo"
            );
        }
        else {
            model.addAttribute("titulo", "Editar gasto");
            model.addAttribute(
                    "acao",
                    "/painel/safras/" + safraId
                            + "/gastos/editar/" + gastoId
            );
        }
    }
}
