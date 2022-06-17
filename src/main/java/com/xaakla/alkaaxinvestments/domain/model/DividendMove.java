package com.xaakla.alkaaxinvestments.domain.model;

import com.xaakla.alkaaxinvestments.api.model.dividendMove.DividendMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveCreateReqModel;
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

    @ManyToOne()
    @JoinColumn(name = "stock_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dividend_moves_stock"))
    private Stock stock;

    @OneToOne()
    @JoinColumn(name = "batch_dividend_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dividend_moves_batch_dividend"))
    private BatchDividend batchDividend;

    public DividendMove(DividendMoveCreateReqModel dividendMoveCreateReqModel, Stock stock, BatchDividend batchDividend) {
        this.quantity = dividendMoveCreateReqModel.getQuantity();
        this.price = dividendMoveCreateReqModel.getPrice();
        this.stock = stock;
        this.batchDividend = batchDividend;
    }
}
