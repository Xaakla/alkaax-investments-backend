package com.xaakla.alkaaxinvestments.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "dividend_moves")
@Entity
public class DividendMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int quantity;

    @NotNull
    private Float price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dividend_moves_stock"))
    private Stock stock;

    @OneToOne()
    @JoinColumn(name = "batch_dividend_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dividend_moves_batch_dividend"))
    private BatchDividend batchDividend;
}
