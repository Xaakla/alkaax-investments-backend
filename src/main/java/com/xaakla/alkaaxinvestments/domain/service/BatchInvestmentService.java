package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentEditReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.GroupInvestmentResModel;
import com.xaakla.alkaaxinvestments.domain.model.BatchInvestment;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMove;
import com.xaakla.alkaaxinvestments.domain.repository.BatchInvestmentRepository;
import com.xaakla.alkaaxinvestments.domain.repository.InvestmentMoveRepository;
import com.xaakla.alkaaxinvestments.domain.repository.StockRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BatchInvestmentService {

    BatchInvestmentRepository batchInvestmentRepository;
    InvestmentMoveRepository investmentMoveRepository;
    StockRepository stockRepository;

    public BatchInvestmentService(BatchInvestmentRepository batchInvestmentRepository, InvestmentMoveRepository investmentMoveRepository, StockRepository stockRepository) {
        this.batchInvestmentRepository = batchInvestmentRepository;
        this.investmentMoveRepository = investmentMoveRepository;
        this.stockRepository = stockRepository;
    }

    public ResponseEntity findAll() { return ResponseEntity.status(200).body(batchInvestmentRepository.findAll()); }

    public ResponseEntity findAllGroups() {
        var groupSet = new HashSet<>();

        batchInvestmentRepository.findAll()
            .forEach(it -> {
                it.setTotal(it.getTotal() / 100);
                var moves = investmentMoveRepository.findAllByBatchInvestment_Id(it.getId());

                moves.forEach(move -> move.setPrice(move.getPrice() / 100));

                groupSet.add(new GroupInvestmentResModel(it, moves));
            });

        return ResponseEntity.status(200).body(groupSet);
    }

    public ResponseEntity findAllGroupsByStockId(Long stockId) {
        var groupSet = new HashSet<>();

        if (!stockRepository.existsById(stockId)) {
            return ResponseEntity.status(404).body("stock id not found");
        }

        batchInvestmentRepository.findAll()
            .forEach(it -> {
                it.setTotal(it.getTotal() / 100);

                var moves = investmentMoveRepository.findAllByStock_IdAndBatchInvestment_Id(stockId, it.getId());
                if (moves.size() > 0) {
                    moves.forEach(move -> move.setPrice(move.getPrice() / 100));
                    groupSet.add(new GroupInvestmentResModel(it, moves));
                }
            });

        return ResponseEntity.ok(groupSet);
    }

    @Transactional
    public ResponseEntity save(BatchInvestmentCreateReqModel batchInvestmentCreateReqModel) {
        var batchInvestment = batchInvestmentRepository.save(new BatchInvestment(batchInvestmentCreateReqModel));

        return ResponseEntity.status(201).body(batchInvestment);
    }

    @Transactional
    public ResponseEntity edit(BatchInvestmentEditReqModel batchInvestmentEditReqModel) {
        if (!batchInvestmentRepository.existsById(batchInvestmentEditReqModel.getId())) {
            return ResponseEntity.status(400).body("Id '"+batchInvestmentEditReqModel.getId()+"' does not exists!");
        }

        var batchInvestment = new BatchInvestment(batchInvestmentEditReqModel, batchInvestmentRepository.getTotal(batchInvestmentEditReqModel.getId()));

        batchInvestmentRepository.save(batchInvestment);

        return ResponseEntity.status(200).body("Batch investment edited successfully");
    }

    @Transactional
    public ResponseEntity deleteById(Long batchInvestmentId) {
        if (!batchInvestmentRepository.existsById(batchInvestmentId)) {
            return ResponseEntity.status(400).body("Id '"+batchInvestmentId+"' does not exists!");
        }

        investmentMoveRepository
                .findAllByBatchInvestment_Id(batchInvestmentId)
                .forEach(it -> {
                    var stock = stockRepository.findById(it.getStock().getId()).orElseThrow(() -> {throw new RuntimeException("Could not find stock id");});
                    stockRepository.updateQuotas(stock.getId(), stock.getQuotas() - it.getQuantity());
                    investmentMoveRepository.deleteById(it.getId());
                });

        batchInvestmentRepository.deleteById(batchInvestmentId);

        return ResponseEntity.status(200).body("Batch investment deleted successfully");
    }
}
