package com.toyproject.pseudo.lotto.domain.statistics;

import com.toyproject.pseudo.lotto.domain.common.ApiResult;
import com.toyproject.pseudo.lotto.web.statistics.StatisticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;


    public ApiResult<List<StatisticsDto>> lucky() {
        List<Statistics> all = statisticsRepository.findAll();
        return ApiResult.success(all.stream()
                .sorted((a, b) -> (int) ((b.getEarn() - b.getSpend()) - (a.getEarn() - a.getSpend())))
                .takeWhile(stat -> stat.getEarn() > stat.getSpend())
                .limit(5)
                .map(StatisticsDto::new)
                .collect(Collectors.toList())
        );
    }

    public ApiResult<List<StatisticsDto>> unlucky() {
        List<Statistics> all = statisticsRepository.findAll();
        return ApiResult.success(all.stream()
                .sorted((a, b) -> (int) ((a.getEarn() - a.getSpend()) - (b.getEarn() - b.getSpend())))
                .takeWhile(stat -> stat.getSpend() > stat.getEarn())
                .limit(5)
                .map(StatisticsDto::new)
                .collect(Collectors.toList())
        );
    }

}
