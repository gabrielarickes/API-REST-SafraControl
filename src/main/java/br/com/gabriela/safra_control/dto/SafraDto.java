package br.com.gabriela.safra_control.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class SafraDto {

    @NotNull(message = "O ano da safra é obrigatório")
    private Integer ano;

    @NotBlank(message = "O tipo de cultura é obrigatório")
    private String tipoCultura;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private Integer quantidade;

    @NotBlank(message = "A unidade de medida é obrigatória")
    private String unidadeMedida;

    @NotNull(message = "O valor da venda é obrigatório")
    @DecimalMin(value = "0.0", message = "O valor de venda não pode ser negativo")
    private BigDecimal valorVendaUnidade;

}
