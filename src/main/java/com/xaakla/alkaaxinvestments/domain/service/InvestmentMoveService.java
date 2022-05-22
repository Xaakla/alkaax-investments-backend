package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentEditReqModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.BatchInvestment;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMove;
import com.xaakla.alkaaxinvestments.domain.repository.BatchInvestmentRepository;
import com.xaakla.alkaaxinvestments.domain.repository.InvestmentMoveRepository;
import com.xaakla.alkaaxinvestments.domain.repository.StockRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InvestmentMoveService {

    InvestmentMoveRepository investmentMoveRepository;
    StockRepository stockRepository;
    BatchInvestmentRepository batchInvestmentRepository;

    public InvestmentMoveService(
            InvestmentMoveRepository investmentMoveRepository,
            StockRepository stockRepository,
            BatchInvestmentRepository batchInvestmentRepository) {
        this.investmentMoveRepository = investmentMoveRepository;
        this.stockRepository = stockRepository;
        this.batchInvestmentRepository = batchInvestmentRepository;
    }

    public ResponseEntity findAll() { return ResponseEntity.status(200).body(investmentMoveRepository.findAll()); }

    public ResponseEntity save(InvestmentMoveCreateReqModel investmentMoveCreateReqModel) {
        investmentMoveRepository.save(
            new InvestmentMove(investmentMoveCreateReqModel,
                stockRepository.findById(investmentMoveCreateReqModel.getStockId())
                    .orElseThrow(() -> { throw new RuntimeException("stockId '"+investmentMoveCreateReqModel.getStockId()+"' does not exists!"); }),
                batchInvestmentRepository.findById(investmentMoveCreateReqModel.getBatchInvestmentId())
                    .orElseThrow(() -> { throw new RuntimeException("batchInvestmentId '"+investmentMoveCreateReqModel.getBatchInvestmentId()+"' does not exists!"); }))
        );

        return ResponseEntity.status(201).body("Investment move created successfully");
    }

    public ResponseEntity edit(InvestmentMoveEditReqModel investmentMoveEditReqModel) {
        if (!investmentMoveRepository.existsById(investmentMoveEditReqModel.getId())) {
            return ResponseEntity.status(400).body("Id '"+investmentMoveEditReqModel.getId()+"' does not exists!");
        }
        if (!stockRepository.existsById(investmentMoveEditReqModel.getStockId())) {
            return ResponseEntity.status(400).body("stockId '"+investmentMoveEditReqModel.getId()+"' does not exists!");
        }
        if (!batchInvestmentRepository.existsById(investmentMoveEditReqModel.getBatchInvestmentId())) {
            return ResponseEntity.status(400).body("batchInvestmentId '"+investmentMoveEditReqModel.getId()+"' does not exists!");
        }

        var investmentMove = investmentMoveRepository.findById(investmentMoveEditReqModel.getId())
            .orElseThrow(() -> { throw new RuntimeException("Could find investment move"); });

        investmentMove.setQuantity(investmentMoveEditReqModel.getQuantity());
        investmentMove.setPrice(investmentMoveEditReqModel.getPrice());
        investmentMove.setStatus(investmentMoveEditReqModel.getStatus());
        investmentMove.setBatchInvestment(batchInvestmentRepository.findById(investmentMoveEditReqModel.getBatchInvestmentId()).get());
        investmentMove.setStock(stockRepository.findById(investmentMoveEditReqModel.getStockId()).get());

        investmentMoveRepository.save(investmentMove);

        return ResponseEntity.status(200).body("Investment move edited successfully");
    }

    public ResponseEntity deleteById(Long investmentMoveId) {
        if (!investmentMoveRepository.existsById(investmentMoveId)) {
            return ResponseEntity.status(400).body("Id '"+investmentMoveId+"' does not exists!");
        }

        investmentMoveRepository.deleteById(investmentMoveId);

        return ResponseEntity.status(200).body("Batch investment deleted successfully");
    }
}
