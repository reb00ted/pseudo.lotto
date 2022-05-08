package com.toyproject.pseudo.lotto.web.user;

import com.toyproject.pseudo.lotto.domain.common.ApiResult;
import com.toyproject.pseudo.lotto.domain.transaction.TransactionService;
import com.toyproject.pseudo.lotto.domain.user.User;
import com.toyproject.pseudo.lotto.domain.user.UserService;
import com.toyproject.pseudo.lotto.web.lotto.LottoDto;
import com.toyproject.pseudo.lotto.web.transaction.BalanceHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("user") UserDto user) {
        return "users/loginForm";
    }

    @PostMapping("/login")
    public String login(
            @Validated @ModelAttribute("user") UserDto user, BindingResult bindingResult,
            HttpServletRequest request,
            @RequestParam(name = "redirectURL", defaultValue = "/") String redirectURL) {
        if (bindingResult.hasErrors()) {
            return "users/loginForm";
        }

        ApiResult<User> result = userService.login(user.getLoginId(), user.getPassword());
        if (!result.isSuccess()) {
            bindingResult.reject("001", result.getMessage());
            return "users/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("userId", result.getResult().getId());
        return "redirect:" + redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/join")
    public String join(@ModelAttribute("user") UserDto user) {
        return "users/joinForm";
    }

    @PostMapping("/join")
    public String joinProcess(@Validated @ModelAttribute("user") UserDto user, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            System.out.println(user.toString());
            return "users/joinForm";
        }

        ApiResult<User> result = userService.join(user);
        if (!result.isSuccess()) {
            bindingResult.reject("002", result.getMessage());
            return "users/joinForm";
        }
        return "redirect:/";
    }

    @GetMapping("/modify")
    public String modify(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "/";
        }
        return "";
    }

    @GetMapping("/purchaseHistory")
    public String purchaseHistory(@SessionAttribute("userId") Long userId, @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                  Model model) {
        ApiResult<Page<LottoDto>> result = transactionService.purchaseHistory(userId, PageRequest.of(pageNum - 1, 10));
        if (!result.isSuccess()) {
            model.addAttribute("err", "몰?루");
            return "users/purchaseHistory";
        }

        Page<LottoDto> lottoDtoPage = result.getResult();
        int currentPage = lottoDtoPage.getNumber() + 1;
        int totalPage = lottoDtoPage.getTotalPages();
        int startPage = ((currentPage - 1) / 10) * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPage);
        List<Integer> pageList = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());
        System.out.printf("QWE %d %d %d\n", lottoDtoPage.getNumber(), lottoDtoPage.getTotalPages(), lottoDtoPage.getTotalElements());

        model.addAttribute("isFirst", lottoDtoPage.isFirst());
        model.addAttribute("isLast", lottoDtoPage.isLast());
        model.addAttribute("lottoList", lottoDtoPage.getContent());
        model.addAttribute("prevPage", currentPage - 1);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", currentPage + 1);
        model.addAttribute("pageList", pageList);
        return "users/purchaseHistory";
    }
    
    @GetMapping("/balanceHistory")
    public String balanceHistory(@SessionAttribute("userId") Long userId, @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                 Model model) {

        ApiResult<Page<BalanceHistoryDto>> result = transactionService.balanceHistory(userId, PageRequest.of(pageNum - 1, 10));
        if (!result.isSuccess()) {
            model.addAttribute("err", "몰?루");
            return "users/balanceHistory";
        }

        Page<BalanceHistoryDto> balanceHistoryDtoPage = result.getResult();
        int currentPage = balanceHistoryDtoPage.getNumber() + 1;
        int totalPage = balanceHistoryDtoPage.getTotalPages();
        int startPage = ((currentPage - 1) / 10) * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPage);
        List<Integer> pageList = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());

        model.addAttribute("isFirst", balanceHistoryDtoPage.isFirst());
        model.addAttribute("isLast", balanceHistoryDtoPage.isLast());
        model.addAttribute("balanceHistoryList", balanceHistoryDtoPage.getContent());
        model.addAttribute("prevPage", currentPage - 1);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", currentPage + 1);
        model.addAttribute("pageList", pageList);
        return "users/balanceHistory";
    }
}
