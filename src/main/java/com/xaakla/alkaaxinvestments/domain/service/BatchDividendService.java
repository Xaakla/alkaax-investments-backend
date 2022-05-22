package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.batchDividend.BatchDividendCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchDividend.BatchDividendEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.BatchDividend;
import com.xaakla.alkaaxinvestments.domain.repository.BatchDividendRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BatchDividendService {

    BatchDividendRepository batchDividendRepository;

    public BatchDividendService(BatchDividendRepository batchDividendRepository) {
        this.batchDividendRepository = batchDividendRepository;
    }

    public ResponseEntity findAll() { return ResponseEntity.status(200).body(batchDividendRepository.findAll()); }

    public ResponseEntity save(BatchDividendCreateReqModel batchDividendCreateReqModel) {
        batchDividendRepository.save(new BatchDividend(batchDividendCreateReqModel));

        return ResponseEntity.status(201).body("Batch dividend created successfully");
    }

    public ResponseEntity edit(BatchDividendEditReqModel batchDividendEditReqModel) {
        if (!batchDividendRepository.existsById(batchDividendEditReqModel.getId())) {
            return ResponseEntity.status(400).body("Id '"+batchDividendEditReqModel.getId()+"' does not exists!");
        }

        var batchDividend = new BatchDividend(batchDividendEditReqModel, batchDividendRepository.getTotal(batchDividendEditReqModel.getId()));

        batchDividendRepository.save(batchDividend);

        return ResponseEntity.status(200).body("Batch dividend edited successfully");
    }

    public ResponseEntity deleteById(Long batchDividendId) {
        if (!batchDividendRepository.existsById(batchDividendId)) {
            return ResponseEntity.status(400).body("Id '"+batchDividendId+"' does not exists!");
        }

        batchDividendRepository.deleteById(batchDividendId);

        return ResponseEntity.status(200).body("Batch dividend deleted successfully");
    }
}
