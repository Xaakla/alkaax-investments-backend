package com.xaakla.alkaaxinvestments.api.model.batchDividend;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BatchDividendEditReqModel {
    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
