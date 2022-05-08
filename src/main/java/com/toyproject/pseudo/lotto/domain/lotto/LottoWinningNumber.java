package com.toyproject.pseudo.lotto.domain.lotto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "lotto_winning_numbers")
@NoArgsConstructor @Getter @Setter
public class LottoWinningNumber {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lotto_round_id")
    private LottoRound round;

    @Min(1) @Max(45)
    @Column(name = "number")
    private Integer number;

    public LottoWinningNumber(LottoRound round, int number) {
        this.round = round;
        this.number = number;
    }

    @Override
    public int hashCode() {
        return (round.getRound().intValue() << 26) | number;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LottoWinningNumber)) {
            return false;
        }
        LottoWinningNumber other = (LottoWinningNumber) obj;
        return round.getRound() == other.getRound().getRound() && number == other.getNumber();
    }
}
