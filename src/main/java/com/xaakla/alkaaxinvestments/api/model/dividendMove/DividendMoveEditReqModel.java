package com.xaakla.alkaaxinvestments.api.model.dividendMove;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DividendMoveEditReqModel {

    @NotNull
    private Long id;

    @NotNull
    private int quantity;

    @NotNull
    private Float price;

    @NotNull
    private Long stockId;

    @NotNull
    private Long batchDividendId;
}
