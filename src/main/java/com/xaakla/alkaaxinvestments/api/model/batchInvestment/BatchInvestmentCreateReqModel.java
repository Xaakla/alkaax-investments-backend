package com.xaakla.alkaaxinvestments.api.model.batchInvestment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BatchInvestmentCreateReqModel {
    @NotBlank
    private String name;
}
