package com.toyproject.pseudo.lotto.domain.lotto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lotto_numbers")
@Getter @Setter @NoArgsConstructor
public class LottoNumber {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lotto_id")
    private Lotto lotto;

    @Column(name = "number")
    private Integer number;

    @Column(name = "matched")
    private boolean matched;

    public LottoNumber(Lotto lotto, Integer number) {
        this.lotto = lotto;
        this.number = number;
    }

    @Override
    public int hashCode() {
        return lotto.getRound().getRound().intValue() ^ number;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LottoNumber)) {
            return false;
        }
        LottoNumber target = (LottoNumber) other;
        return this.lotto.getRound().getRound() == target.getLotto().getRound().getRound()
                && this.number == target.getNumber();
    }


}
