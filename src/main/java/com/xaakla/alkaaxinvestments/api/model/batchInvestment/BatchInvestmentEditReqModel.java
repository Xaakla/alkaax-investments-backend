package com.xaakla.alkaaxinvestments.api.model.batchInvestment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BatchInvestmentEditReqModel {
    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
