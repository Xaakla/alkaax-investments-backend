package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.BatchInvestment;
import com.xaakla.alkaaxinvestments.domain.repository.BatchInvestmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BatchInvestmentService {

    BatchInvestmentRepository batchInvestmentRepository;

    public BatchInvestmentService(BatchInvestmentRepository batchInvestmentRepository) {
        this.batchInvestmentRepository = batchInvestmentRepository;
    }

    public ResponseEntity findAll() { return ResponseEntity.status(200).body(batchInvestmentRepository.findAll()); }

    public ResponseEntity save(BatchInvestmentCreateReqModel batchInvestmentCreateReqModel) {
        batchInvestmentRepository.save(new BatchInvestment(batchInvestmentCreateReqModel));

        return ResponseEntity.status(201).body("Batch investment created successfully");
    }

    public ResponseEntity edit(BatchInvestmentEditReqModel batchInvestmentEditReqModel) {
        if (!batchInvestmentRepository.existsById(batchInvestmentEditReqModel.getId())) {
            return ResponseEntity.status(400).body("Id '"+batchInvestmentEditReqModel.getId()+"' does not exists!");
        }

        var batchInvestment = new BatchInvestment(batchInvestmentEditReqModel, batchInvestmentRepository.getTotal(batchInvestmentEditReqModel.getId()));

        batchInvestmentRepository.save(batchInvestment);

        return ResponseEntity.status(200).body("Batch investment edited successfully");
    }

    public ResponseEntity deleteById(Long batchInvestmentId) {
        if (!batchInvestmentRepository.existsById(batchInvestmentId)) {
            return ResponseEntity.status(400).body("Id '"+batchInvestmentId+"' does not exists!");
        }

        batchInvestmentRepository.deleteById(batchInvestmentId);

        return ResponseEntity.status(200).body("Batch investment deleted successfully");
    }
}
