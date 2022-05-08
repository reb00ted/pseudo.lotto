package com.toyproject.pseudo.lotto.domain.lotto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "lotto_winnings",
       uniqueConstraints = @UniqueConstraint(columnNames = {"lotto_round_id", "ranking"})
)
@Data @NoArgsConstructor
public class LottoWinnings {
    @Id @GeneratedValue
    private Long id;

    @JoinColumn(name = "lotto_round_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private LottoRound round;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "winnings")
    private Long winnings;

    public LottoWinnings(LottoRound round, int ranking, long winnings) {
        this.round = round;
        this.ranking = ranking;
        this.winnings = winnings;
    }
}
