package com.xaakla.alkaaxinvestments.api.model.stock;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StockEditReqModel {
    @NotNull
    private Long id;

    @NotEmpty
    private String code;

    @NotNull
    private int quotas;
}