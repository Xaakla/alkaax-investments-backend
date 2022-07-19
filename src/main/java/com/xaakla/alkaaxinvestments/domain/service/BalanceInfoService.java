package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.domain.model.BatchDividend;
import com.xaakla.alkaaxinvestments.domain.model.BatchInvestment;
import com.xaakla.alkaaxinvestments.domain.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BalanceInfoService {

    StockRepository stockRepository;
    BatchInvestmentRepository batchInvestmentRepository;
    InvestmentMoveRepository investmentMoveRepository;
    BatchDividendRepository batchDividendRepository;
    DividendMoveRepository dividendMoveRepository;

    public BalanceInfoService(StockRepository stockRepository,
                        BatchInvestmentRepository batchInvestmentRepository,
                        InvestmentMoveRepository investmentMoveRepository,
                        BatchDividendRepository batchDividendRepository,
                        DividendMoveRepository dividendMoveRepository) {
        this.stockRepository = stockRepository;
        this.batchInvestmentRepository = batchInvestmentRepository;
        this.investmentMoveRepository = investmentMoveRepository;
        this.batchDividendRepository = batchDividendRepository;
        this.dividendMoveRepository = dividendMoveRepository;
    }

    public ResponseEntity getVariableIncomeTotalBalance() {
        float batchInvestmentTotal = (float) batchInvestmentRepository.findAll()
                .stream().mapToDouble(BatchInvestment::getTotal).sum();

        float batchDividendTotal = (float) batchDividendRepository.findAll()
                .stream().mapToDouble(BatchDividend::getTotal).sum();

        return ResponseEntity.ok((batchDividendTotal + batchInvestmentTotal) / 100);
    }

    public ResponseEntity getVariableIncomeTotalInvestment() {
        return ResponseEntity.ok((float) batchInvestmentRepository.findAll()
                .stream().mapToDouble(BatchInvestment::getTotal).sum() / 100);
    }

    public ResponseEntity getVariableIncomeTotalDividend() {
        return ResponseEntity.ok((float) batchDividendRepository.findAll()
                .stream().mapToDouble(BatchDividend::getTotal).sum() / 100);
    }

    public ResponseEntity getAllStocksQuantity() {
        return ResponseEntity.ok(stockRepository.findAll().size());
    }
}
