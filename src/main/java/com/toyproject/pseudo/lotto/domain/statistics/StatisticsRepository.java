package com.toyproject.pseudo.lotto.domain.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Statistics s WHERE s.user.id = :userId")
    public Statistics findWithLockByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    @Query("SELECT s FROM Statistics s join s.user WHERE s.user.id = :userId")
    public Statistics findWithLockAndJoinByUserId(Long userId);

    @Query("SELECT s FROM Statistics s WHERE s.user.id = :userId")
    public Statistics findByUserId(Long userId);

}
