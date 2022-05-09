package com.toyproject.pseudo.lotto.domain.scheduling;

import com.toyproject.pseudo.lotto.domain.lotto.*;
import com.toyproject.pseudo.lotto.domain.statistics.Statistics;
import com.toyproject.pseudo.lotto.domain.statistics.StatisticsRepository;
import com.toyproject.pseudo.lotto.domain.transaction.BalanceHistory;
import com.toyproject.pseudo.lotto.domain.transaction.BalanceHistoryRepository;
import com.toyproject.pseudo.lotto.domain.transaction.TransactionType;
import com.toyproject.pseudo.lotto.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class LottoRoundScheduler {

    private final LottoRepository lottoRepository;
    private final LottoRoundRepository lottoRoundRepository;
    private final UserRepository userRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final StatisticsRepository statisticsRepository;
    private final Random random;

    @Transactional
    @Scheduled(cron = "0 55 * * * *")
    public void processLastRound() {
        LottoRound lastRound = completeLastRound();
        System.out.printf("%s %d\n", "Hello World!", lastRound.getRound());
        calculation(lastRound.getRound());
        makeNextRound(lastRound);
    }


    public LottoRound completeLastRound() {
        LottoRound currentRound = lottoRoundRepository.findTopByOrderByRoundDesc();
        Set<LottoWinningNumber> winningNumbers = currentRound.getLottoWinningNumbers();

//        LottoRound round = currentRound; // effectively final
        if (winningNumbers.size() < 6) {
            drawWinningNumbers(currentRound, winningNumbers);
            List<Integer> winningsAmount = drawWinnings();
            List<LottoWinnings> winnings = IntStream.rangeClosed(1, 5)
                                                    .mapToObj(i -> new LottoWinnings(currentRound, i, winningsAmount.get(i - 1)))
                                                    .collect(Collectors.toList());

            currentRound.setLottoWinningNumbers(winningNumbers);
            currentRound.setLottoWinnings(winnings);
            lottoRoundRepository.save(currentRound);
        }
        return currentRound;
    }

    private void drawWinningNumbers(LottoRound round, Set<LottoWinningNumber> winningNumbers) {
        while (winningNumbers.size() < 6) {
            winningNumbers.add(new LottoWinningNumber(round, random.nextInt(45) + 1));
        }
    }

    private List<Integer> drawWinnings() {
        List<Integer> winnings = new ArrayList<>(5);
        List<Integer> winningsUpperBound = List.of(100_000, 100_000_000, Integer.MAX_VALUE);

        winnings.add(0);
        winnings.add(5000);
        int prev = 5000;
        for (int upperBound : winningsUpperBound) {
            winnings.add(random.nextInt(upperBound - prev) + prev);
            prev = upperBound;
        }

        Collections.reverse(winnings);
        return winnings;
    }

//    @Transactional
    public void makeNextRound(LottoRound lastRound) {
        LottoRound nextRound = new LottoRound();
        nextRound.setRound(lastRound.getRound() + 1);

        if (LocalDateTime.now().isBefore(lastRound.getEndDate().plusHours(1))) {
            nextRound.setStartDate(lastRound.getStartDate().plusHours(1));
            nextRound.setEndDate(lastRound.getEndDate().plusHours(1));
        } else {
            LocalDateTime now = LocalDateTime.now();
            if (now.getMinute() >= 55) {
                now = now.plusHours(1);
            }
            nextRound.setStartDate(LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0));
            nextRound.setEndDate(LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 55));
        }

        lottoRoundRepository.save(nextRound);
    }

//    @Transactional
    public void calculation(long roundNumber) {
        LottoRound round = lottoRoundRepository.findWithWinnings(roundNumber);
        LottoRound roundWithWinningNumbers = lottoRoundRepository.findWithWinningNumbers(roundNumber);
        round.setLottoWinningNumbers(roundWithWinningNumbers.getLottoWinningNumbers());

        Set<Integer> winningNumbers = round.getWinningNumberOfIntegerList().stream().collect(Collectors.toSet());
        List<Long> winnings = round.getLottoWinnings().stream()
                                                        .map(lottoWinnings -> lottoWinnings.getWinnings())
                                                        .sorted(Comparator.reverseOrder())
                                                        .collect(Collectors.toList());

        // 스케줄러가 싱글 스레드로 동작하고, Lotto, LottoNumber 엔티티는 오직 여기서만 업데이트하고 결과는 항상 같으니 락을 안걸어도 될듯??
        List<Lotto> currentRoundLottoList = lottoRepository.findByRound(roundNumber);
        Map<Long, Long> userBalanceIncrease = new HashMap<>();
        for (Lotto lotto : currentRoundLottoList) {
            int count = 0;
            System.out.println(lotto.getId());
            for (LottoNumber number : lotto.getNumbers()) {
                if (winningNumbers.contains(number.getNumber())) {
                    count++;
                    number.setMatched(true);
                }
            }

            int rank = Math.min(7 - count, 5);
            lotto.setRanking(rank);
            lotto.setWinnings(winnings.get(rank - 1));
            lottoRepository.save(lotto);
            if (rank < 5) {
                Long userId = lotto.getUser().getId();
                userBalanceIncrease.put(userId, userBalanceIncrease.getOrDefault(userId, 0L) + winnings.get(rank - 1));
                BalanceHistory bh = new BalanceHistory(lotto.getUser(), TransactionType.WINNING, lotto, winnings.get(rank - 1));
                balanceHistoryRepository.save(bh);
            }
        };

        // 업데이트하기전에 User 와 Statistics 엔티티 락
        for (Long userId : userBalanceIncrease.keySet()) {
            Statistics statistics = statisticsRepository.findWithLockAndJoinByUserId(userId);
            Long money = userBalanceIncrease.get(userId);
            statistics.getUser().earn(money);
            statistics.earn(money);
            userRepository.save(statistics.getUser());
            statisticsRepository.save(statistics);
        }
    }
}