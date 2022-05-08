package com.toyproject.pseudo.lotto.domain.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {

    @Query("SELECT bh FROM BalanceHistory bh join bh.user WHERE bh.user.id = :userId ORDER BY bh.id DESC")
    Page<BalanceHistory> findByUser(Long userId, Pageable pageable);
}
