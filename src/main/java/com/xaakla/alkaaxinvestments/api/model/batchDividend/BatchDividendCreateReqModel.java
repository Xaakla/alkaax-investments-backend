package com.xaakla.alkaaxinvestments.api.model.batchDividend;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BatchDividendCreateReqModel {
    @NotBlank
    private String name;
}
