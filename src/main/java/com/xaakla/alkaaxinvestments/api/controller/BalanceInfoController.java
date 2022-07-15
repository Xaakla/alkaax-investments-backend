package com.xaakla.alkaaxinvestments.api.controller;

import com.xaakla.alkaaxinvestments.domain.service.BalanceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
