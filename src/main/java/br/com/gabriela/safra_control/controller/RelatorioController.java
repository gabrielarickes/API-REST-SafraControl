package br.com.gabriela.safra_control.controller;

import br.com.gabriela.safra_control.dto.RelatorioDto;
import br.com.gabriela.safra_control.handler.ErroResposta;
import br.com.gabriela.safra_control.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Relatórios", description = "Resultado financeiro das safras (receita, gastos e lucro)")
@ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor",
        content = @Content(schema = @Schema(implementation = ErroResposta.class)))
@RestController
@RequiredArgsConstructor
@RequestMapping("/safra")
public class RelatorioController {

    private final RelatorioService relatorioService;

    // GET mostra JSON para o dashboard e os relatórios.
    @Operation(summary = "Gera o relatório financeiro de uma safra",
            description = "Receita bruta = quantidade x valor de venda por unidade. "
                    + "Lucro líquido = receita bruta - total de gastos.")
    @ApiResponse(responseCode = "200", description = "Relatório da safra")
    @ApiResponse(responseCode = "400", description = "Id com valor não numérico",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Safra não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @GetMapping("/{safraId}/relatorio")
    public RelatorioDto gerarRelatorio(@PathVariable Integer safraId) {
        return relatorioService.gerarRelatorio(safraId);
    }

    // GET mostra JSON com a safra de maior lucro.
    @Operation(summary = "Busca a safra mais lucrativa",
            description = "Retorna o relatório da safra com maior lucro líquido.")
    @ApiResponse(responseCode = "200", description = "Relatório da safra mais lucrativa")
    @ApiResponse(responseCode = "404", description = "Nenhuma safra cadastrada",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @GetMapping("/mais_lucrativa")
    public RelatorioDto buscarMaisLucrativa() {
        return relatorioService.buscarSafraMaisLucrativa();
    }
}
