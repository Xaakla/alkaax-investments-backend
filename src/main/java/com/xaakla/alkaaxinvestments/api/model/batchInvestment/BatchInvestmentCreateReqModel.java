package com.xaakla.alkaaxinvestments.api.model.batchInvestment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BatchInvestmentCreateReqModel {

    private Long id;

    @NotBlank
    private String name;
}
