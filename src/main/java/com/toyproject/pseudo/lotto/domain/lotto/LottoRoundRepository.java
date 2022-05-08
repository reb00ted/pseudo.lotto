package com.toyproject.pseudo.lotto.domain.lotto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LottoRoundRepository extends JpaRepository<LottoRound, Long> {

    // 행이 불필요하게 너무 많이 생성되는 듯?
//    @Query("SELECT lr FROM LottoRound lr join fetch lr.lottoWinnings join fetch lr.lottoWinningNumbers WHERE lr.round = :round")
//    LottoRound findTotalByRound(Long round);
    // @EntityGraph(lottoWinnings, lottoWinningNumbers)
    // LottoRound findTotalByRound(Long round);

    @Query("SELECT distinct lr FROM LottoRound lr join fetch lr.lottoWinnings WHERE lr.round = :round")
    LottoRound findWithWinnings(Long round);

    @Query("SELECT distinct lr FROM LottoRound lr join fetch lr.lottoWinningNumbers WHERE lr.round = :round")
    LottoRound findWithWinningNumbers(Long round);

    LottoRound findByRound(Long round);

    // 현재 진행중인 라운드
    LottoRound findTopByOrderByRoundDesc();

    // 마지막으로 종료된 라운드
    @Query(value = "SELECT MAX(lotto_round_id) FROM lotto_winning_numbers", nativeQuery = true)
    Long findLastRoundNumber();
//
//    //    @Query("SELECT lr FROM LottoRound lr join fetch lr.lottoWinningNumbers WHERE lr.round = (SELECT MAX(r.round) FROM LottoRound r)")
////    @Query("SELECT lr FROM LottoRound lr lr.lottoWinningNumbers ORDER BY lr.round DESC")
////    @EntityGraph(attributePaths = {"lottoWinningNumbers"})
//    @Query("SELECT lr FROM LottoRound lr WHERE EXISTS (SELECT lr2.round FROM LottoRound lr2 join fetch lr.lottoWinningNumbers WHERE lr.round = lr2.round) ORDER BY lr.round DESC")
//    @Query("SELECT ")
//    Page<LottoRound> findTopWithWinningNumbers(Pageable pageable);
//
//    @Query("SELECT lr FROM LottoRound lr join fetch lr.lottoWinningNumbers WHERE lr.endDate < current_timestamp")
//    LottoRound findTopWithWinningNumbers();

}
