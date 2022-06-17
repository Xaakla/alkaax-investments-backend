package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.stock.StockCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.stock.StockEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.Stock;
import com.xaakla.alkaaxinvestments.domain.repository.StockRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public ResponseEntity findAll() {
        return ResponseEntity.status(200).body(stockRepository.findAll());
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

        stockRepository.deleteById(stockId);

        return ResponseEntity.status(200).body("Stock deleted successfully");
    }
}
