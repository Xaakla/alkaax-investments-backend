package com.xaakla.alkaaxinvestments.domain.repository;

import com.xaakla.alkaaxinvestments.domain.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Modifying
    @Query("UPDATE Stock p SET p.quotas = :quotas WHERE p.id = :id")
    void updateQuotas(Long id, int quotas);

    @Query("SELECT quotas FROM Stock WHERE id = :id")
    int getQuotasById(Long id);
}
