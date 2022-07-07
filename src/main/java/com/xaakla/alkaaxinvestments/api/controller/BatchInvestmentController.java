package com.xaakla.alkaaxinvestments.api.controller;

import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentEditReqModel;
import com.xaakla.alkaaxinvestments.domain.service.BatchInvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/batch-investments")
public class BatchInvestmentController {

    @Autowired
    private BatchInvestmentService batchInvestmentService;

    @GetMapping
    public ResponseEntity findAll() { return batchInvestmentService.findAll(); }

    @GetMapping("/groups")
    public ResponseEntity findAllGroups() { return batchInvestmentService.findAllGroups(); }

    @GetMapping("/groups/{stockId}")
    public ResponseEntity findAllGroupsByStockId(@PathVariable Long stockId) {
        return batchInvestmentService.findAllGroupsByStockId(stockId);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid BatchInvestmentCreateReqModel batchInvestmentCreateReqModel) {
        return batchInvestmentService.save(batchInvestmentCreateReqModel);
    }

    @PatchMapping
    public ResponseEntity edit(@RequestBody @Valid BatchInvestmentEditReqModel batchInvestmentEditReqModel) {
        return batchInvestmentService.edit(batchInvestmentEditReqModel);
    }

    @DeleteMapping("/{batchInvestmentId}")
    public ResponseEntity deleteById(@PathVariable Long batchInvestmentId) {
        return batchInvestmentService.deleteById(batchInvestmentId);
    }
}
