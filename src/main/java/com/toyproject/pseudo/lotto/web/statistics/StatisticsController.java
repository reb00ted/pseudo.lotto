package com.toyproject.pseudo.lotto.web.statistics;

import com.toyproject.pseudo.lotto.domain.common.ApiResult;
import com.toyproject.pseudo.lotto.domain.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/lucky")
    public String lucky(Model model) {
        ApiResult<List<StatisticsDto>> lucky = statisticsService.lucky();
        List<StatisticsDto> result = lucky.getResult();
        if (result.size() < 5) {
            result.add(new StatisticsDto("비회원", 0L, 0L, 0L));
        }
        model.addAttribute("statisticsList", result);
        return "statistics/lucky";
    }

    @GetMapping("/unlucky")
    public String unlucky(Model model) {
        ApiResult<List<StatisticsDto>> unlucky = statisticsService.unlucky();
        List<StatisticsDto> result = unlucky.getResult();
        model.addAttribute("statisticsList", result);
        return "statistics/unlucky";
    }

}
