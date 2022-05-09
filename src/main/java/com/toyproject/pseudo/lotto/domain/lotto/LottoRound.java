package com.toyproject.pseudo.lotto.domain.lotto;

import com.toyproject.pseudo.lotto.domain.common.CreationTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "lotto_round")
@Getter
@Setter
@NoArgsConstructor
public class LottoRound extends CreationTimeEntity {
    @Id
    private Long round;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "round", cascade = CascadeType.MERGE)
    private Set<LottoWinningNumber> lottoWinningNumbers = new HashSet<>();

    @OneToMany(mappedBy = "round", cascade = CascadeType.MERGE)
    private List<LottoWinnings> lottoWinnings = new ArrayList<>();

    public List<Integer> getWinningNumberOfIntegerList() {
        return lottoWinningNumbers.stream().map(winningNumber -> winningNumber.getNumber()).collect(Collectors.toList());
    }

    public LottoRound(Long round, LocalDateTime start, LocalDateTime end) {
        this.round = round;
        this.startDate = start;
        this.endDate = end;
    }


    public LottoRound(LottoRound roundWithWinningNumbers, LottoRound roundWithWinnings) {
        if (roundWithWinningNumbers.getRound() != roundWithWinnings.getRound()) {
            throw new IllegalStateException("회차 정보가 다름");
        }
        this.round = roundWithWinningNumbers.getRound();
        this.startDate = roundWithWinningNumbers.getStartDate();
        this.endDate = roundWithWinningNumbers.getEndDate();
        this.lottoWinningNumbers = roundWithWinningNumbers.getLottoWinningNumbers();
        this.lottoWinnings = roundWithWinnings.getLottoWinnings();
    }
}
