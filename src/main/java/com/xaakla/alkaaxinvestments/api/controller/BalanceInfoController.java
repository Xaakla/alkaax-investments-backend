package com.xaakla.alkaaxinvestments.api.controller;

import com.xaakla.alkaaxinvestments.domain.service.BalanceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance-info")
public class BalanceInfoController {

    @Autowired
    BalanceInfoService balanceInfoService;

    @GetMapping("/variable-income-total-balance")
    public ResponseEntity getVariableIncomeTotalBalance() {
        return balanceInfoService.getVariableIncomeTotalBalance();
    }

    @GetMapping("/variable-income-investment")
    public ResponseEntity getVariableIncomeInvestment() {
        return balanceInfoService.getVariableIncomeTotalInvestment();
    }

    @GetMapping("/variable-income-dividend")
    public ResponseEntity getVariableIncomeDividend() {
        return balanceInfoService.getVariableIncomeTotalDividend();
    }

    @GetMapping("/stocks-quantity")
    public ResponseEntity getAllStocksQuantity() { return balanceInfoService.getAllStocksQuantity(); }

    @GetMapping("/variable-income-investment/{stockId}")
    public ResponseEntity getVariableIncomeInvestmentByStockId(@PathVariable Long stockId) {
        return balanceInfoService.getVariableIncomeInvestmentByStockId(stockId);
    }

    @GetMapping("/variable-income-dividend/{stockId}")
    public ResponseEntity getVariableIncomeDividendByStockId(@PathVariable Long stockId) {
        return balanceInfoService.getVariableIncomeDividendByStockId(stockId);
    }
}
