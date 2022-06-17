package com.xaakla.alkaaxinvestments.api.model.stock;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class StockCreateReqModel {
    @NotEmpty
    private String code;
}
