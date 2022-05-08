package com.toyproject.pseudo.lotto.web.statistics;

import com.toyproject.pseudo.lotto.domain.common.StringUtil;
import com.toyproject.pseudo.lotto.domain.statistics.Statistics;
import lombok.Data;


@Data
public class StatisticsDto {

    private String loginId;
    private String spend;
    private String earn;
    private String netProfit;

    public StatisticsDto(Statistics statistics) {
        this.loginId = statistics.getUser().getLoginId();
        this.spend = StringUtil.toMoneyFormat(statistics.getSpend());
        this.earn = StringUtil.toMoneyFormat(statistics.getEarn());
        this.netProfit = StringUtil.toMoneyFormat(statistics.getEarn() - statistics.getSpend());
    }

    public StatisticsDto(String loginId, Long spend, Long earn, Long netProfit) {
        this.loginId = loginId;
        this.spend = StringUtil.toMoneyFormat(spend);
        this.earn = StringUtil.toMoneyFormat(earn);
        this.netProfit = StringUtil.toMoneyFormat(netProfit);
    }
}
