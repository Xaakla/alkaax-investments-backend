package com.xaakla.alkaaxinvestments.domain.model;

import com.xaakla.alkaaxinvestments.api.model.batchDividend.BatchDividendCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchDividend.BatchDividendEditReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentEditReqModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "batch_dividends")
@Entity
public class BatchDividend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Float total;

    public BatchDividend(BatchDividendCreateReqModel batchDividendCreateReqModel) {
        this.name = batchDividendCreateReqModel.getName();
        this.total = 0f;
    }

    public BatchDividend(BatchDividendEditReqModel batchDividendEditReqModel, Float total) {
        this.id = batchDividendEditReqModel.getId();
        this.name = batchDividendEditReqModel.getName();
        this.total = total;
    }
}
