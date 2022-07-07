package com.xaakla.alkaaxinvestments.domain.repository;

import com.xaakla.alkaaxinvestments.domain.model.InvestmentMove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvestmentMoveRepository extends JpaRepository<InvestmentMove, Long> {

    @Modifying
    @Query("UPDATE InvestmentMove p SET p.stock.id = :stockId WHERE p.id = :id")
    void updateInvestmentStock(Long stockId, Long id);

    List<InvestmentMove> findAllByBatchInvestment_Id(Long id);

    List<InvestmentMove> findAllByStock_Id(Long id);

    List<InvestmentMove> findAllByStock_IdAndBatchInvestment_Id(Long stockId, Long batchInvestmentId);
}
