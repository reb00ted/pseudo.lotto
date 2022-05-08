package com.toyproject.pseudo.lotto.domain.lotto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LottoRepository extends JpaRepository<Lotto, Long> {

    @Query("SELECT distinct l FROM Lotto l join fetch l.numbers WHERE l.round.round = :round")
    List<Lotto> findByRound(Long round);

    @Query("SELECT l FROM Lotto l join l.user join l.round WHERE l.user.id = :userId ORDER BY l.id DESC")
    Page<Lotto> findByUserIdWithPaging(Long userId, Pageable pageable);

//    @Query("SELECT new com.toy.pseudo.lottery.domain.lotto.WinningUserDto(l.ranking, count(*)) FROM Lotto l WHERE l.round = :round GROUP BY l.ranking")
//    List<WinningUserDto> findWinningUser(Long round);
}
