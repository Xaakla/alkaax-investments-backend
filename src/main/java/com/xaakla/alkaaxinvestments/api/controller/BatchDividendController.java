package com.xaakla.alkaaxinvestments.api.controller;

import com.xaakla.alkaaxinvestments.api.model.batchDividend.BatchDividendCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchDividend.BatchDividendEditReqModel;
import com.xaakla.alkaaxinvestments.domain.service.BatchDividendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/batch-dividends")
public class BatchDividendController {

    @Autowired
    private BatchDividendService batchDividendService;

    @GetMapping
    public ResponseEntity findAll() { return batchDividendService.findAll(); }

    @GetMapping("/groups")
    public ResponseEntity findAllGroups() { return batchDividendService.findAllGroups(); }

    @GetMapping("/groups/{stockId}")
    public ResponseEntity findAllGroupsByStock(@PathVariable Long stockId) {
        return batchDividendService.findAllGroupsByStockId(stockId);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid BatchDividendCreateReqModel batchDividendCreateReqModel) {
        return batchDividendService.save(batchDividendCreateReqModel);
    }

    @PatchMapping
    public ResponseEntity edit(@RequestBody @Valid BatchDividendEditReqModel batchDividendEditReqModel) {
        return batchDividendService.edit(batchDividendEditReqModel);
    }

    @DeleteMapping("/{batchDividendId}")
    public ResponseEntity deleteById(@PathVariable Long batchDividendId) {
        return batchDividendService.deleteById(batchDividendId);
    }
}
