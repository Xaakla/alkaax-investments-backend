package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.dividendMove.DividendMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.dividendMove.DividendMoveEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.DividendMove;
import com.xaakla.alkaaxinvestments.domain.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DividendMoveService {

    DividendMoveRepository dividendMoveRepository;
    StockRepository stockRepository;
    BatchDividendRepository batchDividendRepository;

    public DividendMoveService(
            DividendMoveRepository dividendMoveRepository,
            StockRepository stockRepository,
            BatchDividendRepository batchDividendRepository) {
        this.dividendMoveRepository = dividendMoveRepository;
        this.stockRepository = stockRepository;
        this.batchDividendRepository = batchDividendRepository;
    }

    public ResponseEntity findAll() { return ResponseEntity.status(200).body(dividendMoveRepository.findAll()); }

    @Transactional
    public ResponseEntity save(DividendMoveCreateReqModel dividendMoveCreateReqModel)
    {
        if (!batchDividendRepository.existsById(dividendMoveCreateReqModel.getBatchDividendId())) {
            return ResponseEntity.status(400).body("batchInvestmentId '"+dividendMoveCreateReqModel.getBatchDividendId()+"' does not exists!");
        }
        dividendMoveRepository.save(
                new DividendMove(dividendMoveCreateReqModel,
                        stockRepository.findById(dividendMoveCreateReqModel.getStockId())
                                .orElseThrow(() -> { throw new RuntimeException("stockId '"+dividendMoveCreateReqModel.getStockId()+"' does not exists!"); }),
                        batchDividendRepository.findById(dividendMoveCreateReqModel.getBatchDividendId()).get())
        );

        Float newTotal = batchDividendRepository.getTotal(dividendMoveCreateReqModel.getBatchDividendId())
                + (dividendMoveCreateReqModel.getPrice() * dividendMoveCreateReqModel.getQuantity());

        batchDividendRepository.updateTotal(newTotal, dividendMoveCreateReqModel.getBatchDividendId());

        return ResponseEntity.status(201).body("Dividend move created successfully");
    }

    @Transactional
    public ResponseEntity edit(DividendMoveEditReqModel dividendMoveEditReqModel) {
        if (!dividendMoveRepository.existsById(dividendMoveEditReqModel.getId())) {
            return ResponseEntity.status(400).body("Id '"+dividendMoveEditReqModel.getId()+"' does not exists!");
        }
        if (!stockRepository.existsById(dividendMoveEditReqModel.getStockId())) {
            return ResponseEntity.status(400).body("stockId '"+dividendMoveEditReqModel.getId()+"' does not exists!");
        }
        if (!batchDividendRepository.existsById(dividendMoveEditReqModel.getBatchDividendId())) {
            return ResponseEntity.status(400).body("batchDividendId '"+dividendMoveEditReqModel.getId()+"' does not exists!");
        }

        var dividendMove = dividendMoveRepository.findById(dividendMoveEditReqModel.getId())
                .orElseThrow(() -> { throw new RuntimeException("Could find investment move"); });

        Float newTotal =
                (batchDividendRepository.getTotal(dividendMoveEditReqModel.getBatchDividendId())
                - (dividendMove.getPrice() * dividendMove.getQuantity()))
                + (dividendMoveEditReqModel.getPrice() * dividendMoveEditReqModel.getQuantity());

        dividendMove.setQuantity(dividendMoveEditReqModel.getQuantity());
        dividendMove.setPrice(dividendMoveEditReqModel.getPrice());
        dividendMove.setBatchDividend(batchDividendRepository.findById(dividendMoveEditReqModel.getBatchDividendId()).get());
        dividendMove.setStock(stockRepository.findById(dividendMoveEditReqModel.getStockId()).get());

        dividendMoveRepository.save(dividendMove);
        batchDividendRepository.updateTotal(newTotal, dividendMoveEditReqModel.getBatchDividendId());

        return ResponseEntity.status(200).body("Dividend move edited successfully");
    }

    @Transactional
    public ResponseEntity deleteById(Long dividendMoveId) {
        if (!dividendMoveRepository.existsById(dividendMoveId)) {
            return ResponseEntity.status(400).body("Id '"+dividendMoveId+"' does not exists!");
        }

        dividendMoveRepository.deleteById(dividendMoveId);

        return ResponseEntity.status(200).body("Dividend move deleted successfully");
    }
}