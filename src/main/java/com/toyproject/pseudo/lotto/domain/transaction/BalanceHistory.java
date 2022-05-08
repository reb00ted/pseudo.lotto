package com.toyproject.pseudo.lotto.domain.transaction;

import com.toyproject.pseudo.lotto.domain.common.CreationTimeEntity;
import com.toyproject.pseudo.lotto.domain.lotto.Lotto;
import com.toyproject.pseudo.lotto.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "balance_history")
@Data @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BalanceHistory extends CreationTimeEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "lotto_id")
    private Lotto lotto;

    @Column(name = "amount")
    private Long amount;

    public BalanceHistory(User user, TransactionType type, Lotto lotto, Long amount) {
        this.user = user;
        this.type = type;
        this.lotto = lotto;
        this.amount = amount;
    }
}
