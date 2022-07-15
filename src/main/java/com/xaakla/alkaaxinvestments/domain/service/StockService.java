package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.stock.StockCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.stock.StockEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMove;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMoveStatus;
import com.xaakla.alkaaxinvestments.domain.model.Stock;
import com.xaakla.alkaaxinvestments.domain.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class StockService {

    StockRepository stockRepository;
    BatchInvestmentRepository batchInvestmentRepository;
    InvestmentMoveRepository investmentMoveRepository;
    BatchDividendRepository batchDividendRepository;
    DividendMoveRepository dividendMoveRepository;

    public StockService(StockRepository stockRepository,
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

    public ResponseEntity findAll() {
        return ResponseEntity.status(200).body(stockRepository.findAll());
    }

    public ResponseEntity findOneById(Long stockId) {
        return ResponseEntity.ok(stockRepository.findById(stockId)
                .orElseThrow(() -> {throw new RuntimeException("stock id not found");}));
    }

    @Transactional
    public ResponseEntity save(StockCreateReqModel stockCreateReqModel) {
        stockRepository.save(new Stock(stockCreateReqModel));

        return ResponseEntity.status(201).body("Stock created sucessfully!");
    }

    @Transactional
    public ResponseEntity edit(StockEditReqModel stockEditReqModel) {
        if (!stockRepository.existsById(stockEditReqModel.getId())) {
            return ResponseEntity.status(400).body("Id '"+stockEditReqModel.getId()+"' does not exists!");
        }

        stockRepository.save(new Stock(stockEditReqModel));

        return ResponseEntity.status(200).body("Stock edited successfully");
    }

    @Transactional
    public ResponseEntity deleteById(Long stockId) {
        if (!stockRepository.existsById(stockId)) {
            return ResponseEntity.status(400).body("Id '"+stockId+"' does not exists!");
        }

        investmentMoveRepository.deleteAllByStock_Id(stockId);
        dividendMoveRepository.deleteAllByStock_Id(stockId);

        batchInvestmentRepository.findAll().forEach(batchInvestment -> {
            batchInvestment.setTotal(0f);

            var investmentMoveList = new ArrayList<>(investmentMoveRepository.findAllByBatchInvestment_Id(batchInvestment.getId()));

            if (investmentMoveList.size() == 0) {
                batchInvestmentRepository.deleteById(batchInvestment.getId());
                return;
            }

            investmentMoveList.forEach(investmentMove -> {
                batchInvestment.setTotal(
                    investmentMove.getStatus() == InvestmentMoveStatus.BUY ?
                        (batchInvestment.getTotal() + (investmentMove.getPrice() * investmentMove.getQuantity())) :
                        (batchInvestment.getTotal() - (investmentMove.getPrice() * investmentMove.getQuantity())));
            });
            batchInvestmentRepository.updateTotal(batchInvestment.getTotal(), batchInvestment.getId());
        });

        batchDividendRepository.findAll().forEach(batchDividend -> {

            var dividendMoveList = new ArrayList<>(dividendMoveRepository.findAllByBatchDividend_Id(batchDividend.getId()));

            if (dividendMoveList.size() == 0) {
                batchDividendRepository.deleteById(batchDividend.getId());
                return;
            }

            batchDividend.setTotal(0f);
            dividendMoveRepository.findAllByBatchDividend_Id(batchDividend.getId())
                    .forEach(dividendMove -> {
                        batchDividend.setTotal(batchDividend.getTotal() + (dividendMove.getPrice() * dividendMove.getQuantity()));
                    });
            batchDividendRepository.updateTotal(batchDividend.getTotal(), batchDividend.getId());
        });

        stockRepository.deleteById(stockId);

        return ResponseEntity.status(200).body("Stock deleted successfully");
    }
}
