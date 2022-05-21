package com.xaakla.alkaaxinvestments.api.controller;

import com.xaakla.alkaaxinvestments.api.model.stock.StockCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.stock.StockEditReqModel;
import com.xaakla.alkaaxinvestments.domain.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity findAll() {
        return stockService.findAll();
    }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid StockCreateReqModel stockCreateReqModel) {
        return stockService.save(stockCreateReqModel);
    }

    @PatchMapping
    public ResponseEntity edit(@RequestBody @Valid StockEditReqModel stockEditReqModel) {
        return stockService.edit(stockEditReqModel);
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity deleteById(@PathVariable Long stockId) {
        return stockService.deleteById(stockId);
    }
}
