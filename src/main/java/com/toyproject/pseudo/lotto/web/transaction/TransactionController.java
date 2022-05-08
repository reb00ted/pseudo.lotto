package com.toyproject.pseudo.lotto.web.transaction;

import com.toyproject.pseudo.lotto.domain.common.ApiResult;
import com.toyproject.pseudo.lotto.domain.lotto.Lotto;
import com.toyproject.pseudo.lotto.domain.lotto.LottoRound;
import com.toyproject.pseudo.lotto.domain.transaction.TransactionService;
import com.toyproject.pseudo.lotto.web.lotto.LottoNumberDto;
import com.toyproject.pseudo.lotto.web.lotto.LottoRoundDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TransactionController {
    
    private final TransactionService transactionService;

    @GetMapping("/point/charge")
    public String chargeForm() {
        return "point/charge";
    }

    @PostMapping("/point/charge")
    public String charge(@SessionAttribute("userId") Long userId, Long point, Model model) {
        if (point < 0 || point > 1_000_000) {
            model.addAttribute("err", "유효하지 않은 요청입니다.");
            return "point/charge";
        }
        ApiResult<Object> result = transactionService.charge(userId, point);
        if (!result.isSuccess()) {
            model.addAttribute("err", result.getMessage());
            return "/point/charge";
        }
        return "redirect:/";
    }

    @GetMapping("/point/withdraw")
    public String withdrawForm() {
        return "point/withdraw";
    }

    @PostMapping("/point/withdraw")
    public String withdraw(@SessionAttribute("userId") Long userId, Long point, Model model) {
        if (point < 0) {
            model.addAttribute("err", "유효하지 않은 요청입니다.");
            return "point/withdraw";
        }

        ApiResult<Object> result = transactionService.withdraw(userId, point);
        if (!result.isSuccess()) {
            model.addAttribute("err", result.getMessage());
            return "point/withdraw";
        }
        return "redirect:/";
    }

    @GetMapping("/lotto/buy")
    public String lottoForm(Model model) {
        ApiResult<LottoRound> result = transactionService.getCurrentLottoRound();
        model.addAttribute("lottoRound", result.getResult());
        return "lotto/buy";
    }

    @PostMapping("/lotto/buy")
    public String lottoBuy(@Validated LottoNumberDto lottoNumberDto, BindingResult bindingResult,
                           @SessionAttribute("userId") Long userId, @RequestParam(name = "round") long round,
                           Model model) {
        model.addAttribute("lottoRound", transactionService.getCurrentLottoRound().getResult());
        if (bindingResult.hasErrors()) {
            model.addAttribute("err", "유효하지 않은 요청입니다.");
            return "lotto/buy";
        }

        ApiResult<Lotto> result = transactionService.buy(userId, round, lottoNumberDto);
        if (!result.isSuccess()) {
            model.addAttribute("err", result.getMessage());
            return "lotto/buy";
        }

        List<Integer> numberList = result.getResult().getNumbers().stream().map(lottoNumber -> lottoNumber.getNumber()).sorted().collect(Collectors.toList());
        model.addAttribute("numberList", numberList);
        return "lotto/buy";
    }

    @GetMapping("/lotto/pastHistory")
    public String pastHistory(Model model) {
        ApiResult<List<Long>> roundList = transactionService.getPastLottoRoundList();
        Long lastRound = roundList.getResult().get(0);
        ApiResult<LottoRoundDto> result = transactionService.pastHistory(lastRound);
        model.addAttribute("roundList", roundList.getResult());
        model.addAttribute("lottoRound", result.getResult());
        return "lotto/pastHistory";
    }

    @PostMapping("/lotto/pastHistory")
    public String pastHistory(@RequestParam(name = "round") Long round, Model model) {
        ApiResult<List<Long>> roundList = transactionService.getPastLottoRoundList();
        ApiResult<LottoRoundDto> result = transactionService.pastHistory(round);
        if (!result.isSuccess()) {
            model.addAttribute("err", result.getMessage());
            model.addAttribute("roundList", roundList.getResult());
            return "lotto/pastHistory";
        }
        model.addAttribute("roundList", roundList.getResult());
        model.addAttribute("lottoRound", result.getResult());
        return "lotto/pastHistory";
    }
}
