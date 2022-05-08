package com.toyproject.pseudo.lotto.domain.scheduling;

import com.toyproject.pseudo.lotto.domain.lotto.LottoRound;
import com.toyproject.pseudo.lotto.domain.lotto.LottoRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StartUp implements ApplicationRunner {

    private final LottoRoundRepository lottoRoundRepository;
    private final LottoRoundScheduler lottoRoundScheduler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LottoRound top = lottoRoundRepository.findTopByOrderByRoundDesc();
        if (top == null) {
            LocalDateTime now = LocalDateTime.now();
            if (now.getMinute() >= 55) {
                now = now.plusHours(1);
            }
            LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0);
            LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 55);
            lottoRoundRepository.save(new LottoRound(1L, start, end));
        } else if (top.getEndDate().isBefore(LocalDateTime.now())) {
//            lottoRoundScheduler.calculation(top.getRound());
//            lottoRoundScheduler.makeNextRound(top);
            lottoRoundScheduler.processLastRound();
        }
    }
}
