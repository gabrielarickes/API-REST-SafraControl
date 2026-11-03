package br.com.gabriela.safra_control.dto;

import jakarta.persistence.Column;
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

    @NotNull
    private Integer ano;

    @NotBlank
    private String tipoCultura;

    @NotNull
    private Integer quantidade;

    @NotBlank
    private String unidadeMedida;

    @NotNull
    private BigDecimal valorVendaUnidade;

}
