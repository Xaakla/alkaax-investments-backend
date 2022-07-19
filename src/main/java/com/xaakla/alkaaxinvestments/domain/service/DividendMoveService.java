package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.dividendMove.DividendMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.dividendMove.DividendMoveEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.DividendMove;
import com.xaakla.alkaaxinvestments.domain.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity saveAll(List<DividendMoveCreateReqModel> dividendMoveCreateReqModelList)
    {
        var dividendMoves = dividendMoveCreateReqModelList.stream().map(it -> {
            it.setPrice(it.getPrice() * 100);
            var stock = stockRepository.findById(it.getStockId()).orElseThrow(() -> {throw new RuntimeException("Stock ID not found");});
            var batchDividend = batchDividendRepository
                    .findById(it.getBatchDividendId()).orElseThrow(() -> {throw new RuntimeException("Batch Dividend ID not found");});

            batchDividend.setTotal((batchDividend.getTotal() + (it.getPrice() * it.getQuantity())));
            batchDividendRepository.updateTotal(batchDividend.getTotal(), batchDividend.getId());

            return new DividendMove(it, stock, batchDividend);
        }).collect(Collectors.toList());

        dividendMoveRepository.saveAll(dividendMoves);

        return ResponseEntity.status(201).body("Dividend Moves created successfully");
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
