package com.xaakla.alkaaxinvestments.domain.repository;

import com.xaakla.alkaaxinvestments.domain.model.DividendMove;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DividendMoveRepository extends JpaRepository<DividendMove, Long> {

    List<DividendMove> findAllByBatchDividend_Id(Long id);

    List<DividendMove> findAllByStock_IdAndBatchDividend_Id(Long stockId, Long batchDividendId);
}
