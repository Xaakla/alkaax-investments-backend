package com.xaakla.alkaaxinvestments.domain.repository;

import com.xaakla.alkaaxinvestments.domain.model.BatchInvestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BatchInvestmentRepository extends JpaRepository<BatchInvestment, Long> {

    @Query("SELECT total FROM BatchInvestment WHERE id = :id")
    Float getTotal(Long id);

    @Modifying
    @Query("UPDATE BatchInvestment p SET p.total = :newTotal WHERE p.id = :id")
    void updateTotal(Float newTotal, Long id);
}
