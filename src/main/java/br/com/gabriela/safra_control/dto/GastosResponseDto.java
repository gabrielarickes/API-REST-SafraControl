package br.com.gabriela.safra_control.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastosResponseDto (
        Integer id,
        String tipoGasto,
        String descricao,
        BigDecimal valor,
        LocalDate dataGasto,
        Integer safraId
) {}