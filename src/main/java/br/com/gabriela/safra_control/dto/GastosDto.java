package br.com.gabriela.safra_control.dto;

import jakarta.validation.constraints.DecimalMin;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GastosDto {

    @NotBlank(message = "O tipo do gasto é obrigatório")
    private String tipoGasto;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;


    @NotNull(message = "O valor do gasto é obrigatório")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "O valor do gasto deve ser maior que zero"
    )
    private BigDecimal valor;



    @NotNull(message = "A data do gasto é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataGasto;


}

