package com.xaakla.alkaaxinvestments.api.controller;

import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentEditReqModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveEditReqModel;
import com.xaakla.alkaaxinvestments.domain.service.BatchInvestmentService;
import com.xaakla.alkaaxinvestments.domain.service.InvestmentMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/investment-move")
public class InvestmentMoveController {

    @Autowired
    private InvestmentMoveService investmentMoveService;

    @GetMapping
    public ResponseEntity findAll() { return investmentMoveService.findAll(); }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid InvestmentMoveCreateReqModel investmentMoveCreateReqModel) {
        return investmentMoveService.save(investmentMoveCreateReqModel);
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
