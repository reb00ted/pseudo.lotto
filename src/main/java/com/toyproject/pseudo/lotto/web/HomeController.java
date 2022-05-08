package com.toyproject.pseudo.lotto.web;

import com.toyproject.pseudo.lotto.domain.common.ApiResult;
import com.toyproject.pseudo.lotto.domain.transaction.TransactionService;
import com.toyproject.pseudo.lotto.domain.user.User;
import com.toyproject.pseudo.lotto.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping("/")
    public String home(@CookieValue(name = "userId", required = false) Long userId, Model model) {
//        ApiResult<LottoRoundStatistics> lottoRoundStatisticsApiResult = transactionService.lastRoundWinningData();
//        model.addAttribute("statistics", lottoRoundStatisticsApiResult.getResult());

        if (userId == null) {
            return "index";
        }

        ApiResult<User> user = userService.findById(userId);
        if (!user.isSuccess()) {
            return "index";
        }
        model.addAttribute("user", user.getResult());
        return "index";
    }
}
