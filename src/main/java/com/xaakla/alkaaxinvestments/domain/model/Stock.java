package com.xaakla.alkaaxinvestments.domain.model;

import com.xaakla.alkaaxinvestments.api.model.stock.StockCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.stock.StockEditReqModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "stocks")
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String code;

    @NotNull
    private int quotas;

    public Stock(StockCreateReqModel stockCreateReqModel) {
        this.code = stockCreateReqModel.getCode();
        this.quotas = stockCreateReqModel.getQuotas();
    }

    public Stock(StockEditReqModel stockEditReqModel) {
        this.id = stockEditReqModel.getId();
        this.code = stockEditReqModel.getCode();
        this.quotas = stockEditReqModel.getQuotas();
    }
}
