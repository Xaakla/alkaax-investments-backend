package com.xaakla.alkaaxinvestments.domain.repository;

import com.xaakla.alkaaxinvestments.domain.model.InvestmentMove;
import com.xaakla.alkaaxinvestments.domain.model.InvestmentMoveStatus;
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

    void deleteAllByStock_Id(Long stockId);

    @Query("SELECT quantity FROM InvestmentMove WHERE id = :id")
    int getQuantityById(Long id);

    int getQuantityByStock_Id(Long stockId);

    @Query("SELECT status FROM InvestmentMove WHERE id = :id")
    InvestmentMoveStatus getInvestmentMoveStatusById(Long id);
}
