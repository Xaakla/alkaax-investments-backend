package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMove;
import com.xaakla.alkaaxinvestments.domain.repository.BatchInvestmentRepository;
import com.xaakla.alkaaxinvestments.domain.repository.InvestmentMoveRepository;
import com.xaakla.alkaaxinvestments.domain.repository.StockRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public ResponseEntity saveAll(List<InvestmentMoveCreateReqModel> investmentMoveCreateReqModelList) {
        var investmentMoves = investmentMoveCreateReqModelList.stream().map(it -> {
            return new InvestmentMove(it,
                stockRepository.findById(it.getStockId()).orElseThrow(() -> {throw new RuntimeException("Stock ID not found");}),
                batchInvestmentRepository.findById(it.getBatchInvestmentId()).orElseThrow(() -> {throw new RuntimeException("Batch Investment ID not found");}));
        }).collect(Collectors.toList());

        investmentMoveRepository.saveAll(investmentMoves);

        return ResponseEntity.status(201).body("Investments Moves created successfully");
    }

    @Transactional
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

        Float newTotal =
                (batchInvestmentRepository.getTotal(investmentMoveEditReqModel.getBatchInvestmentId())
                - (investmentMove.getPrice() * investmentMove.getQuantity()))
                + (investmentMoveEditReqModel.getPrice() * investmentMoveEditReqModel.getQuantity());

        investmentMove.setQuantity(investmentMoveEditReqModel.getQuantity());
        investmentMove.setPrice(investmentMoveEditReqModel.getPrice());
        investmentMove.setStatus(investmentMoveEditReqModel.getStatus());
        investmentMove.setBatchInvestment(batchInvestmentRepository.findById(investmentMoveEditReqModel.getBatchInvestmentId()).get());
//        investmentMove.setStock(stockRepository.findById(investmentMoveEditReqModel.getStockId()).get());

        // implementar esse metodo ao inves do comentado acima
        investmentMoveRepository.updateInvestmentStock(investmentMoveEditReqModel.getStockId(), investmentMoveEditReqModel.getId());

        investmentMoveRepository.save(investmentMove);
        batchInvestmentRepository.updateTotal(newTotal, investmentMoveEditReqModel.getBatchInvestmentId());

        return ResponseEntity.status(200).body("Investment move edited successfully");
    }

    @Transactional
    public ResponseEntity deleteById(Long investmentMoveId) {
        if (!investmentMoveRepository.existsById(investmentMoveId)) {
            return ResponseEntity.status(400).body("Id '"+investmentMoveId+"' does not exists!");
        }

        investmentMoveRepository.deleteById(investmentMoveId);

        return ResponseEntity.status(200).body("Batch investment deleted successfully");
    }
}
