package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.batchDividend.BatchDividendCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchDividend.BatchDividendEditReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchDividend.GroupDividendResModel;
import com.xaakla.alkaaxinvestments.domain.model.BatchDividend;
import com.xaakla.alkaaxinvestments.domain.repository.BatchDividendRepository;
import com.xaakla.alkaaxinvestments.domain.repository.DividendMoveRepository;
import com.xaakla.alkaaxinvestments.domain.repository.StockRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class BatchDividendService {

    BatchDividendRepository batchDividendRepository;
    DividendMoveRepository dividendMoveRepository;
    StockRepository stockRepository;

    public BatchDividendService(StockRepository stockRepository, BatchDividendRepository batchDividendRepository, DividendMoveRepository dividendMoveRepository) {
        this.batchDividendRepository = batchDividendRepository;
        this.dividendMoveRepository = dividendMoveRepository;
        this.stockRepository = stockRepository;
    }

    public ResponseEntity findAll() { return ResponseEntity.status(200).body(batchDividendRepository.findAll()); }

    public ResponseEntity findAllGroups() {
        var groupSet = new HashSet<>();

        batchDividendRepository.findAll()
                .forEach(it -> {
                    it.setTotal(it.getTotal() / 100);
                    var moves = dividendMoveRepository.findAllByBatchDividend_Id(it.getId());

                    moves.forEach(move -> move.setPrice(move.getPrice() / 100));

                    groupSet.add(new GroupDividendResModel(it, moves));
                });

        return ResponseEntity.status(200).body(groupSet);
    }

    public ResponseEntity findAllGroupsByStockId(Long stockId) {
        var groupSet = new HashSet<>();

        if (!stockRepository.existsById(stockId)) {
            return ResponseEntity.status(404).body("stock id not found");
        }

        batchDividendRepository.findAll()
                .forEach(it -> {
                    it.setTotal(it.getTotal() / 100);

                    var moves = dividendMoveRepository.findAllByStock_IdAndBatchDividend_Id(stockId, it.getId());
                    if (moves.size() > 0) {
                        moves.forEach(move -> move.setPrice(move.getPrice() / 100));
                        groupSet.add(new GroupDividendResModel(it, moves));
                    }
                });

        return ResponseEntity.ok(groupSet);
    }

    @Transactional
    public ResponseEntity save(BatchDividendCreateReqModel batchDividendCreateReqModel) {
        var batchDividend = batchDividendRepository.save(new BatchDividend(batchDividendCreateReqModel));

        return ResponseEntity.status(201).body(batchDividend);
    }

    @Transactional
    public ResponseEntity edit(BatchDividendEditReqModel batchDividendEditReqModel) {
        if (!batchDividendRepository.existsById(batchDividendEditReqModel.getId())) {
            return ResponseEntity.status(400).body("Id '"+batchDividendEditReqModel.getId()+"' does not exists!");
        }

        var batchDividend = new BatchDividend(batchDividendEditReqModel, batchDividendRepository.getTotal(batchDividendEditReqModel.getId()));

        batchDividendRepository.save(batchDividend);

        return ResponseEntity.status(200).body("Batch dividend edited successfully");
    }

    @Transactional
    public ResponseEntity deleteById(Long batchDividendId) {
        if (!batchDividendRepository.existsById(batchDividendId)) {
            return ResponseEntity.status(400).body("Id '"+batchDividendId+"' does not exists!");
        }

        batchDividendRepository.deleteById(batchDividendId);

        return ResponseEntity.status(200).body("Batch dividend deleted successfully");
    }
}
