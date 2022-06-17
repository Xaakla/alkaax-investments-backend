package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.batchInvestment.BatchInvestmentEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.BatchInvestment;
import com.xaakla.alkaaxinvestments.domain.repository.BatchInvestmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatchInvestmentService {

    BatchInvestmentRepository batchInvestmentRepository;

    public BatchInvestmentService(BatchInvestmentRepository batchInvestmentRepository) {
        this.batchInvestmentRepository = batchInvestmentRepository;
    }

    public ResponseEntity findAll() { return ResponseEntity.status(200).body(batchInvestmentRepository.findAll()); }

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

        batchInvestmentRepository.deleteById(batchInvestmentId);

        return ResponseEntity.status(200).body("Batch investment deleted successfully");
    }
}
