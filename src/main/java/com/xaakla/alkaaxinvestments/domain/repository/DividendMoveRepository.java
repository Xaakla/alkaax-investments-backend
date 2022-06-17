package com.xaakla.alkaaxinvestments.domain.repository;

import com.xaakla.alkaaxinvestments.domain.model.DividendMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DividendMoveRepository extends JpaRepository<DividendMove, Long> {
}
