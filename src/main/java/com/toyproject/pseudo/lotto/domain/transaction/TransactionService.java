package com.toyproject.pseudo.lotto.domain.transaction;

import com.toyproject.pseudo.lotto.domain.common.ApiResult;
import com.toyproject.pseudo.lotto.domain.lotto.*;
import com.toyproject.pseudo.lotto.domain.statistics.Statistics;
import com.toyproject.pseudo.lotto.domain.statistics.StatisticsRepository;
import com.toyproject.pseudo.lotto.domain.user.User;
import com.toyproject.pseudo.lotto.domain.user.UserRepository;
import com.toyproject.pseudo.lotto.web.lotto.LottoDto;
import com.toyproject.pseudo.lotto.web.lotto.LottoNumberDto;
import com.toyproject.pseudo.lotto.web.lotto.LottoRoundDto;
import com.toyproject.pseudo.lotto.web.transaction.BalanceHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final LottoRepository lottoRepository;
    private final LottoRoundRepository lottoRoundRepository;
    private final StatisticsRepository statisticsRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final Random random;

//    @Transactional(readOnly = true)
//    public ApiResult<LottoRoundStatistics> lastRoundWinningData() {
//        Long lastRound = lottoRoundRepository.findLastRoundNumber();
//
//        List<WinningUserDto> winningUserDtoList = lottoRepository.findWinningUser(lastRound);
//        Collections.sort(winningUserDtoList, Comparator.comparingInt(WinningUserDto::getRanking));
//        List<Integer> winningUsers = winningUserDtoList.stream()
//                .map(winningUserDto -> winningUserDto.getUserNumber())
//                .collect(Collectors.toList());
//
//        LottoRound withWinnings = lottoRoundRepository.findWithWinnings(lastRound);
//        List<String> winningsList =  withWinnings.getLottoWinnings().stream()
//                .sorted((a, b) -> (int) (b.getWinnings() - a.getWinnings()))
//                .map(winnings -> StringUtil.toMoneyFormat(winnings.getWinnings()))
//                .collect(Collectors.toList());
//
//        LottoRoundStatistics statistics = new LottoRoundStatistics(lastRound, winningUsers, winningsList);
//        return ApiResult.success(statistics);
//    }

    @Transactional
    public ApiResult<Object> charge(Long userId, Long amount) {
        User user = userRepository.findWithLockById(userId);
        if (user == null) {
            return ApiResult.fail("유저정보를 찾을 수 없습니다.");
        }
        user.charge(amount);

        BalanceHistory bh = new BalanceHistory(user, TransactionType.CHARGE, null, amount);
        balanceHistoryRepository.save(bh);
        return ApiResult.success(null);
    }

    @Transactional
    public ApiResult<Object> withdraw(Long userId, Long amount) {
        User user = userRepository.findWithLockById(userId);
        if (user == null) {
            return ApiResult.fail("유저정보를 찾을 수 없습니다.");
        }
        if (user.getBalance() < amount) {
            return ApiResult.fail("출금하려는 금액이 현재 보유하고 있는 포인트보다 많습니다.");
        }
        user.withdraw(amount);

        BalanceHistory bh = new BalanceHistory(user, TransactionType.WITHDRAW, null, -amount);
        balanceHistoryRepository.save(bh);
        return ApiResult.success(null);
    }

    @Transactional(readOnly = true)
    public ApiResult<LottoRound> getCurrentLottoRound() {
        LottoRound round = lottoRoundRepository.findTopByOrderByRoundDesc();
        return ApiResult.success(round);
    }

    @Transactional
    public ApiResult<Lotto> buy(Long userId, Long round, LottoNumberDto numbers) {
        User user = userRepository.findWithLockById(userId);
        if (user == null) {
            return ApiResult.fail("유저 정보를 찾을 수 없습니다.");
        }
        if (user.getBalance() < 1000) {
            return ApiResult.fail("포인트가 부족하여 구입할 수 없습니다. 충전후 이용해주세요.");
        }

        LottoRound lottoRound = lottoRoundRepository.findByRound(round);
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(lottoRound.getStartDate()) || now.isAfter(lottoRound.getEndDate())) {
            return ApiResult.fail("현재 판매 중인 복권이 아닙니다. 판매 기간을 확인해주세요.");
        }

        Set<Integer> lottoNumbers = numbers.getNumbers().stream().collect(Collectors.toSet());
        while (lottoNumbers.size() < 6) {
            lottoNumbers.add(random.nextInt(45) + 1);
        }

        Lotto lotto = Lotto.builder()
                .user(user)
                .round(lottoRound)
                .build();
        lotto.setNumbersByIntegerSet(lottoNumbers);

        user.spend(1000);
        Lotto result = lottoRepository.save(lotto);
        Statistics statistics = statisticsRepository.findByUserId(user.getId());
        statistics.spend(1000L);
        balanceHistoryRepository.save(new BalanceHistory(user, TransactionType.BUY, result, -1000L));
        return ApiResult.success(result);
    }


    @Transactional(readOnly = true)
    public ApiResult<LottoRoundDto> pastHistory(Long round) {
        LottoRound result = lottoRoundRepository.findWithWinningNumbers(round);
        if (result == null) {
            return ApiResult.fail(String.format("%d 회차 정보를 찾을 수 없습니다.\n", round));
        }
        return ApiResult.success(new LottoRoundDto(result));
    }


    @Transactional(readOnly = true)
    public ApiResult<List<Long>> getPastLottoRoundList() {
//        Long lastRound = lottoWinningNumberRepository.findLastRoundNumber();
        Long lastRound = lottoRoundRepository.findLastRoundNumber();
        return ApiResult.success(LongStream.range(0, lastRound)
                    .map(i -> lastRound - i)
                    .boxed()
                    .collect(Collectors.toList())
        );
    }


    @Transactional(readOnly = true)
    public ApiResult<Page<LottoDto>> purchaseHistory(Long userId, Pageable pageable) {
        Page<Lotto> lottoPage = lottoRepository.findByUserIdWithPaging(userId, pageable);
        Page<LottoDto> result = lottoPage.map(lotto -> new LottoDto(lotto));
        return ApiResult.success(result);
    }

    @Transactional(readOnly = true)
    public ApiResult<Page<BalanceHistoryDto>> balanceHistory(Long userId, Pageable pageable) {
        Page<BalanceHistory> bh = balanceHistoryRepository.findByUser(userId, pageable);
        Page<BalanceHistoryDto> result = bh.map(balanceHistory -> new BalanceHistoryDto(balanceHistory));
        return ApiResult.success(result);
    }
}
