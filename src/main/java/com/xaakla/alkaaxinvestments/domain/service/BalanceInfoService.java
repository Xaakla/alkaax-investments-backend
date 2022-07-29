package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.domain.model.BatchDividend;
import com.xaakla.alkaaxinvestments.domain.model.BatchInvestment;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMoveStatus;
import com.xaakla.alkaaxinvestments.domain.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class BalanceInfoService {

    StockRepository stockRepository;
    BatchInvestmentRepository batchInvestmentRepository;
    BatchInvestmentService batchInvestmentService;
    InvestmentMoveRepository investmentMoveRepository;
    BatchDividendRepository batchDividendRepository;
    DividendMoveRepository dividendMoveRepository;

    public BalanceInfoService(StockRepository stockRepository,
                        BatchInvestmentRepository batchInvestmentRepository,
                        InvestmentMoveRepository investmentMoveRepository,
                        BatchDividendRepository batchDividendRepository,
                        DividendMoveRepository dividendMoveRepository, BatchInvestmentService batchInvestmentService) {
        this.stockRepository = stockRepository;
        this.batchInvestmentRepository = batchInvestmentRepository;
        this.investmentMoveRepository = investmentMoveRepository;
        this.batchDividendRepository = batchDividendRepository;
        this.dividendMoveRepository = dividendMoveRepository;
        this.batchInvestmentService = batchInvestmentService;
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

    public ResponseEntity getVariableIncomeInvestmentByStockId(Long stockId) {
        if (!stockRepository.existsById(stockId)) {
            return ResponseEntity.status(404).body("stock id not found");
        }

        AtomicReference<Float> total = new AtomicReference<>(0f);
        investmentMoveRepository.findAllByStock_Id(stockId).forEach(it -> {
            if (it.getStatus() == InvestmentMoveStatus.BUY) {
                total.set(total.get() + it.getPrice() * it.getQuantity());
            } else {
                total.set(total.get() - it.getPrice() * it.getQuantity());
            }
        });

        return ResponseEntity.ok(total.get() / 100);
    }

    public ResponseEntity getVariableIncomeDividendByStockId(Long stockId) {
        if (!stockRepository.existsById(stockId)) {
            return ResponseEntity.status(404).body("stock id not found");
        }

        var total = (float) dividendMoveRepository.findAllByStock_Id(stockId)
                .stream().mapToDouble(it -> it.getPrice() * it.getQuantity()).sum();

        return ResponseEntity.ok(total / 100);
    }
}
