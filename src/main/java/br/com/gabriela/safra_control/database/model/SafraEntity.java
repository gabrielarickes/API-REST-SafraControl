package br.com.gabriela.safra_control.database.model;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;

@Entity
@Table(name = "safra", uniqueConstraints = @UniqueConstraint(columnNames = {"ano", "tipo_cultura"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SafraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false, name = "tipo_cultura")
    private String tipoCultura;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "unidade_de_medida", nullable = false)
    private String unidadeMedida;

    @Column(name = "valor_de_venda_por_unidade", nullable = false)
    private BigDecimal valorVendaUnidade;
}
