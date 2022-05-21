package com.xaakla.alkaaxinvestments.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "investment_moves")
@Entity
public class InvestmentMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int quantity;

    @NotNull
    private Float price;

    @NotEmpty
    private InvestmentMoveStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", nullable = false, foreignKey = @ForeignKey(name = "fk_investment_moves_stock"))
    private Stock stock;

    @OneToOne()
    @JoinColumn(name = "batch_investment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_investment_moves_batch_investment"))
    private BatchInvestment batchInvestment;
}
