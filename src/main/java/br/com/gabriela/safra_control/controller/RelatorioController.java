package br.com.gabriela.safra_control.controller;

import br.com.gabriela.safra_control.dto.RelatorioDto;
import br.com.gabriela.safra_control.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/safra")
public class RelatorioController {

    private final RelatorioService relatorioService;

    // GET mostra JSON para o dashboard e os relatórios.
    @GetMapping("/{safraId}/relatorio")
    public RelatorioDto gerarRelatorio(@PathVariable Integer safraId) {
        return relatorioService.gerarRelatorio(safraId);
    }

    // GET mostra JSON com a safra de maior lucro.
    @GetMapping("/mais_lucrativa")
    public RelatorioDto buscarMaisLucrativa() {
        return relatorioService.buscarSafraMaisLucrativa();
    }
}
