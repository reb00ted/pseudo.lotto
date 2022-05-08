package com.toyproject.pseudo.lotto.web.lotto;

import com.toyproject.pseudo.lotto.domain.common.StringUtil;
import com.toyproject.pseudo.lotto.domain.lotto.LottoRound;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor @AllArgsConstructor
public class LottoRoundDto {
    private Long round;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Integer> lottoWinningNumbers;
    private List<String> lottoWinnings = new ArrayList<>();


    public LottoRoundDto(LottoRound round) {
        this.round = round.getRound();
        this.startDate = round.getStartDate();
        this.endDate = round.getEndDate();
        System.out.println("로또 번호 추출");
        this.lottoWinningNumbers = round.getLottoWinningNumbers().stream()
                .map(lottoWinningNumber -> lottoWinningNumber.getNumber())
                .sorted()
                .collect(Collectors.toList());
        System.out.println("당첨 금액 추출");
        this.lottoWinnings = round.getLottoWinnings().stream()
                .map(lottoWinnings -> lottoWinnings.getWinnings())
                .sorted(Comparator.reverseOrder())
                .map(StringUtil::toMoneyFormat)
                .collect(Collectors.toList());
    }
    
    public LottoRoundDto(LottoRound roundWithWinningNumbers, LottoRound roundWithWinnings) {
        if (roundWithWinningNumbers.getRound() != roundWithWinnings.getRound()) {
            throw new IllegalStateException("회차 정보가 다름");
        }
        this.round = roundWithWinningNumbers.getRound();
        this.startDate = roundWithWinningNumbers.getStartDate();
        this.endDate = roundWithWinningNumbers.getEndDate();
        this.lottoWinningNumbers = roundWithWinningNumbers.getLottoWinningNumbers().stream()
                .map(lottoWinningNumber -> lottoWinningNumber.getNumber())
                .sorted()
                .collect(Collectors.toList());
        this.lottoWinnings = roundWithWinnings.getLottoWinnings().stream()
                .map(lottoWinnings -> lottoWinnings.getWinnings())
                .sorted(Comparator.reverseOrder())
                .map(StringUtil::toMoneyFormat)
                .collect(Collectors.toList());
    }
}
