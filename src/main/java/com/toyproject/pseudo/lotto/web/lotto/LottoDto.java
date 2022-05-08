package com.toyproject.pseudo.lotto.web.lotto;

import com.toyproject.pseudo.lotto.domain.common.StringUtil;
import com.toyproject.pseudo.lotto.domain.lotto.Lotto;
import com.toyproject.pseudo.lotto.domain.user.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LottoDto {
    private Long id;
    private User user;
    private Long round;
    private List<LottoNumberMatchedDto> numbers;
    private Integer ranking;
    private String winnings;
    private LocalDateTime buyAt;

    public LottoDto(Lotto lotto) {
        this.id = lotto.getId();
        this.user = lotto.getUser();
        this.round = lotto.getRound().getRound();
        this.numbers = lotto.getNumbers().stream()
                .sorted((a, b) -> a.getNumber() - b.getNumber())
                .map(LottoNumberMatchedDto::new)
                .collect(Collectors.toList());
        this.ranking = lotto.getRanking();
        this.winnings = lotto.getWinnings() == null ? "" : StringUtil.toMoneyFormat(lotto.getWinnings());
        this.buyAt = lotto.getCreatedAt();
    }
}