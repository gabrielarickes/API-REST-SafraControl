package br.com.gabriela.safra_control.service;

import br.com.gabriela.safra_control.database.model.GastosEntity;
import br.com.gabriela.safra_control.database.model.SafraEntity;
import br.com.gabriela.safra_control.database.repository.IGastosRepository;
import br.com.gabriela.safra_control.database.repository.ISafraRepository;
import br.com.gabriela.safra_control.dto.RelatorioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final ISafraRepository safraRepository;
    private final IGastosRepository gastosRepository;

    //Relatório
    public RelatorioDto gerarRelatorio(Integer safraId){

        SafraEntity safra = safraRepository.findById(safraId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Safra "+safraId+ " não encontrada"));

        List<GastosEntity> gastos = gastosRepository.findBySafra_Id(safraId);

        RelatorioDto relatorio = new RelatorioDto();

        relatorio.setSafraId(safra.getId());
        relatorio.setAno(safra.getAno());
        relatorio.setTipoCultura(safra.getTipoCultura());
        relatorio.setQuantidade(safra.getQuantidade());
        relatorio.setUnidadeMedida(safra.getUnidadeMedida());
        relatorio.setValorVendaUnidade(safra.getValorVendaUnidade());

        BigDecimal quantidade = BigDecimal.valueOf(relatorio.getQuantidade());
        //Calcular o valor bruto
        BigDecimal receitaBruta = quantidade.multiply(safra.getValorVendaUnidade());

        relatorio.setReceitaBruta(receitaBruta);

        //Calcular gastos
        BigDecimal totalGastos = BigDecimal.ZERO;

        for(GastosEntity gasto : gastos){
            totalGastos = totalGastos.add(gasto.getValor());
        }
        relatorio.setTotalGastos(totalGastos);

        //Lucro liquido
        BigDecimal lucroLiquido = receitaBruta.subtract(totalGastos);
        relatorio.setLucroLiquido(lucroLiquido);

        return relatorio;
    }

    //Safra que teve mais lucro
    public RelatorioDto buscarSafraMaisLucrativa(){

        List<SafraEntity> safras = safraRepository.findAll();

        if (safras.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhuma safra cadastrada"
            );
        }

        RelatorioDto maisLucrativa = null;
        
        for(SafraEntity safra : safras){
            RelatorioDto relatorioAtual = gerarRelatorio(safra.getId());
            
            if(maisLucrativa == null ||
                relatorioAtual.getLucroLiquido().compareTo(maisLucrativa.getLucroLiquido()) > 0) {
                maisLucrativa = relatorioAtual;
            }
        }
        return maisLucrativa;
    }
}
