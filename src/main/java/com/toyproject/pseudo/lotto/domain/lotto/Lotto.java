package com.toyproject.pseudo.lotto.domain.lotto;

import com.toyproject.pseudo.lotto.domain.common.CreationTimeEntity;
import com.toyproject.pseudo.lotto.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "lotto")
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Lotto extends CreationTimeEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "round_id")
    private LottoRound round;

    @OneToMany(mappedBy = "lotto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LottoNumber> numbers;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "winnings")
    private Long winnings;

    public void setNumbersByIntegerSet(Set<Integer> numberList) {
        this.numbers = numberList.stream()
                                    .map(i -> new LottoNumber(this, i))
                                    .collect(Collectors.toSet());
    }

    public Set<Integer> getNumberListByIntegerList() {
        return this.numbers.stream()
                                .map(lottoNumber -> lottoNumber.getNumber())
                                .collect(Collectors.toSet());
    }
}
