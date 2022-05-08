package com.toyproject.pseudo.lotto.domain.lotto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

// 미사용
@Data
@AllArgsConstructor
public class LottoRoundStatistics {
    Long round;
    List<Integer> winningUserNumber;
    List<String> winnings;

}
