package com.xaakla.alkaaxinvestments.domain.model;

import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveEditReqModel;
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
    @Enumerated(EnumType.STRING)
    private InvestmentMoveStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", nullable = false, foreignKey = @ForeignKey(name = "fk_investment_moves_stock"))
    private Stock stock;

    @OneToOne()
    @JoinColumn(name = "batch_investment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_investment_moves_batch_investment"))
    private BatchInvestment batchInvestment;

    public InvestmentMove(InvestmentMoveCreateReqModel investmentMoveCreateReqModel, Stock stock, BatchInvestment batchInvestment) {
        this.quantity = investmentMoveCreateReqModel.getQuantity();
        this.price = investmentMoveCreateReqModel.getPrice();
        this.status = investmentMoveCreateReqModel.getStatus();
        this.stock = stock;
        this.batchInvestment = batchInvestment;
    }

    public InvestmentMove(InvestmentMoveEditReqModel investmentMoveEditReqModel, Stock stock, BatchInvestment batchInvestment) {
        this.id = investmentMoveEditReqModel.getId();
        this.quantity = investmentMoveEditReqModel.getQuantity();
        this.price = investmentMoveEditReqModel.getPrice();
        this.status = investmentMoveEditReqModel.getStatus();
        this.stock = stock;
        this.batchInvestment = batchInvestment;
    }
}
