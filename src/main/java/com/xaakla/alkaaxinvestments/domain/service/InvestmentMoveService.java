package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveCreateReqModel;
import com.xaakla.alkaaxinvestments.api.model.investmentMove.InvestmentMoveEditReqModel;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMove;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMoveStatus;
import com.xaakla.alkaaxinvestments.domain.repository.BatchInvestmentRepository;
import com.xaakla.alkaaxinvestments.domain.repository.InvestmentMoveRepository;
import com.xaakla.alkaaxinvestments.domain.repository.StockRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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
            it.setPrice(it.getPrice() * 100);
            var stock = stockRepository.findById(it.getStockId()).orElseThrow(() -> {throw new RuntimeException("Stock ID not found");});
            var batchInvestment = batchInvestmentRepository
                .findById(it.getBatchInvestmentId()).orElseThrow(() -> {throw new RuntimeException("Batch Investment ID not found");});

            batchInvestment.setTotal(
                    it.getStatus() == InvestmentMoveStatus.BUY ?
                            (batchInvestment.getTotal() + (it.getPrice() * it.getQuantity())) :
                            (batchInvestment.getTotal() - (it.getPrice() * it.getQuantity())));
            batchInvestmentRepository.updateTotal(batchInvestment.getTotal(), batchInvestment.getId());

            // if exists
            if (it.getId() != null) {
                // se agora for comprar
                if (it.getStatus() == InvestmentMoveStatus.BUY) {
                    // verifica se antes era vender
                    if (investmentMoveRepository.getInvestmentMoveStatusById(it.getId()) == InvestmentMoveStatus.SELL) {
                        // pega as quotas atuais, some as quotas que ele vendeu e soma as quotas que ele quer comprar
                        stock.setQuotas((stock.getQuotas() + investmentMoveRepository.getQuantityById(it.getId())) + it.getQuantity());
                    } else {
                        // pega as quotas atuais, diminui as quotas que ele comprou e soma as quotas que ele quer comprar
                        stock.setQuotas((stock.getQuotas() - investmentMoveRepository.getQuantityById(it.getId())) + it.getQuantity());
                    }
                // se for vender
                } else {
                    // verifica se antes era comprar
                    if (investmentMoveRepository.getInvestmentMoveStatusById(it.getId()) == InvestmentMoveStatus.BUY) {
                        // pega as quotas atuais, diminui as quotas que ele comprou e diminui as quotas que ele quer vender
                        stock.setQuotas((stock.getQuotas() - investmentMoveRepository.getQuantityById(it.getId())) - it.getQuantity());
                    } else {
                        // pega as quotas atuais, soma as quotas que ele vendeu e diminui as quotas que ele quer vender
                        stock.setQuotas((stock.getQuotas() + investmentMoveRepository.getQuantityById(it.getId())) - it.getQuantity());
                    }
                }
            } else {
                stock.setQuotas(it.getStatus() == InvestmentMoveStatus.BUY ? (stock.getQuotas() + it.getQuantity()) : (stock.getQuotas() - it.getQuantity()));
            }

            stockRepository.updateQuotas(stock.getId(), stock.getQuotas());

            return new InvestmentMove(it, stock, batchInvestment);
        }).collect(Collectors.toList());

        investmentMoveRepository.saveAll(investmentMoves);

        return ResponseEntity.status(201).body("Investment Moves created successfully");
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
    public ResponseEntity deleteAllByIds(List<Long> ids) {
        ids.forEach(id -> {
            var investmentMove = investmentMoveRepository.findById(id).orElseThrow(() -> {throw new RuntimeException("Investment Move Id not found");});
            var stock = stockRepository.findById(investmentMove.getStock().getId()).orElseThrow(() -> {throw new RuntimeException("Stock ID not found");});

            // se agora for comprar
            if (investmentMove.getStatus() == InvestmentMoveStatus.BUY) {
                // verifica se antes era vender
                if (investmentMoveRepository.getInvestmentMoveStatusById(investmentMove.getId()) == InvestmentMoveStatus.SELL) {
                    // pega as quotas atuais, some as quotas que ele vendeu e soma as quotas que ele quer comprar
                    stock.setQuotas((stock.getQuotas() + investmentMoveRepository.getQuantityById(investmentMove.getId())) + investmentMove.getQuantity());
                } else {
                    // pega as quotas atuais, diminui as quotas que ele comprou e soma as quotas que ele quer comprar
                    stock.setQuotas((stock.getQuotas() - investmentMoveRepository.getQuantityById(investmentMove.getId())) + investmentMove.getQuantity());
                }
                // se for vender
            } else {
                // verifica se antes era comprar
                if (investmentMoveRepository.getInvestmentMoveStatusById(investmentMove.getId()) == InvestmentMoveStatus.BUY) {
                    // pega as quotas atuais, diminui as quotas que ele comprou e diminui as quotas que ele quer vender
                    stock.setQuotas((stock.getQuotas() - investmentMoveRepository.getQuantityById(investmentMove.getId())) - investmentMove.getQuantity());
                } else {
                    // pega as quotas atuais, soma as quotas que ele vendeu e diminui as quotas que ele quer vender
                    stock.setQuotas((stock.getQuotas() + investmentMoveRepository.getQuantityById(investmentMove.getId())) - investmentMove.getQuantity());
                }
            }

            stockRepository.updateQuotas(stock.getId(), stock.getQuotas());
        });

        investmentMoveRepository.deleteAllById(ids);

        return ResponseEntity.ok("All investment moves deleted successfully!");
    }

    @Transactional
    public ResponseEntity deleteById(Long investmentMoveId) {
        if (!investmentMoveRepository.existsById(investmentMoveId)) {
            return ResponseEntity.status(400).body("Id '"+investmentMoveId+"' does not exists!");
        }

        var move = investmentMoveRepository.findById(investmentMoveId).get();
        var newTotal = batchInvestmentRepository.getTotal(move.getBatchInvestment().getId()) - (move.getPrice() * move.getQuantity());

        batchInvestmentRepository.updateTotal(newTotal, move.getBatchInvestment().getId());
        stockRepository.updateQuotas(move.getStock().getId(), move.getStock().getQuotas() - move.getQuantity());

        investmentMoveRepository.deleteById(investmentMoveId);

        return ResponseEntity.status(200).body("Batch investment deleted successfully");
    }
}
