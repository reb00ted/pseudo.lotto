package com.toyproject.pseudo.lotto.web.transaction;

import com.toyproject.pseudo.lotto.domain.common.StringUtil;
import com.toyproject.pseudo.lotto.domain.transaction.BalanceHistory;
import com.toyproject.pseudo.lotto.domain.user.User;
import com.toyproject.pseudo.lotto.web.lotto.LottoDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BalanceHistoryDto {
    private Long id;
    private User user;
    private String type;
    private LottoDto lotto;
    private String amount;
    private LocalDateTime at;

    public BalanceHistoryDto(BalanceHistory bh) {
        this.id = bh.getId();
        this.user = bh.getUser();
        this.type = bh.getType().getValue();
        this.lotto = bh.getLotto() != null ? new LottoDto(bh.getLotto()) : null;
        this.amount = StringUtil.toMoneyFormat(bh.getAmount());
        this.at = bh.getCreatedAt();
    }
}
