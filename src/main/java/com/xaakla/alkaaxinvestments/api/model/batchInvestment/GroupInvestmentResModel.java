package com.xaakla.alkaaxinvestments.api.model.batchInvestment;

import com.xaakla.alkaaxinvestments.domain.model.BatchInvestment;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMove;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class GroupInvestmentResModel {
    @NotEmpty
    private Long id;

    @NotBlank
    private final String name;

    @NotNull
    private Float total;

    @Valid
    private List<InvestmentMove> moves;

    public GroupInvestmentResModel(BatchInvestment bt, List<InvestmentMove> iml) {
        this.id = bt.getId();
        this.name = bt.getName();
        this.total = bt.getTotal();
        this.moves = iml;
    }
}
