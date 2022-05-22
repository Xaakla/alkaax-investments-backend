package com.xaakla.alkaaxinvestments.domain.repository;

import com.xaakla.alkaaxinvestments.domain.model.InvestmentMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentMoveRepository extends JpaRepository<InvestmentMove, Long> {
}
