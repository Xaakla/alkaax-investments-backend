package com.xaakla.alkaaxinvestments.api.controller;

import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveEditAndSaveAllModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveEditReqModel;
import com.xaakla.alkaaxinvestments.domain.service.InvestmentMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/investment-move")
public class InvestmentMoveController {

    @Autowired
    private InvestmentMoveService investmentMoveService;

    @GetMapping
    public ResponseEntity findAll() { return investmentMoveService.findAll(); }

    @PostMapping
    public ResponseEntity saveAll(@RequestBody @Valid List<InvestmentMoveCreateReqModel> investmentMoveCreateReqModelArray) {
        return investmentMoveService.saveAll(investmentMoveCreateReqModelArray);
    }

    @PatchMapping
    public ResponseEntity edit(@RequestBody @Valid InvestmentMoveEditReqModel investmentMoveEditReqModel) {
        return investmentMoveService.edit(investmentMoveEditReqModel);
    }

    @DeleteMapping("/{investmentMoveId}")
    public ResponseEntity deleteById(@PathVariable Long investmentMoveId) {
        return investmentMoveService.deleteById(investmentMoveId);
    }
}
