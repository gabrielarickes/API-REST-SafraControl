package br.com.gabriela.safra_control.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RelatorioDto {

    private Integer safraId;
    private Integer ano;
    private String tipoCultura;
    private Integer quantidade;
    private String unidadeMedida;


    private BigDecimal valorVendaUnidade;
    private BigDecimal receitaBruta;
    private BigDecimal totalGastos;
    private BigDecimal lucroLiquido;
}
