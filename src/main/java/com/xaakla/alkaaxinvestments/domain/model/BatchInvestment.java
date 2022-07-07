package com.xaakla.alkaaxinvestments.domain.model;

import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentEditReqModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "batch_investments")
@Entity
public class BatchInvestment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Float total;

    public BatchInvestment(BatchInvestmentCreateReqModel batchInvestmentCreateReqModel) {
        this.name = batchInvestmentCreateReqModel.getName();
        this.total = 0f;
    }

    public BatchInvestment(BatchInvestmentEditReqModel batchInvestmentEditReqModel, Float total) {
        this.id = batchInvestmentEditReqModel.getId();
        this.name = batchInvestmentEditReqModel.getName();
        this.total = total;
    }
}
