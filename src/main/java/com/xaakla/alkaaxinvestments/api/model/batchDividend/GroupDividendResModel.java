package com.xaakla.alkaaxinvestments.api.model.batchDividend;

import com.xaakla.alkaaxinvestments.domain.model.BatchDividend;
import com.xaakla.alkaaxinvestments.domain.model.DividendMove;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class GroupDividendResModel {
    @NotEmpty
    private Long id;

    @NotBlank
    private final String name;

    @NotNull
    private Float total;

    @Valid
    private List<DividendMove> moves;

    public GroupDividendResModel(BatchDividend bt, List<DividendMove> iml) {
        this.id = bt.getId();
        this.name = bt.getName();
        this.total = bt.getTotal();
        this.moves = iml;
    }
}
