package com.xaakla.alkaaxinvestments.domain.repository;

import com.xaakla.alkaaxinvestments.domain.model.BatchDividend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BatchDividendRepository extends JpaRepository<BatchDividend, Long> {

    @Query("SELECT total FROM BatchDividend WHERE id = :id")
    Float getTotal(Long id);

    @Modifying
    @Query("UPDATE BatchDividend p SET p.total = :newTotal WHERE p.id = :id")
    void updateTotal(Float newTotal, Long id);
}
