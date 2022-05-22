package com.xaakla.alkaaxinvestments.api.model.investmentMove;

import com.xaakla.alkaaxinvestments.domain.model.InvestmentMoveStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InvestmentMoveEditReqModel {
    @NotNull
    private Long id;

    @NotNull
    private int quantity;

    @NotNull
    private Float price;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    private InvestmentMoveStatus status;

    @NotNull
    private Long stockId;

    @NotNull
    private Long batchInvestmentId;
}
