package com.toyproject.pseudo.lotto.domain.statistics;

import com.toyproject.pseudo.lotto.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "statistics")
@Data
@NoArgsConstructor
public class Statistics {
    @Id @GeneratedValue
    private Long id;

    @JoinColumn(name = "user")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column(name = "spend")
    private Long spend;

    @Column(name = "earn")
    private Long earn;

    public Statistics(User user) {
        this.user = user;
        this.spend = 0L;
        this.earn = 0L;
    }

    public void spend(Long amount) {
        spend += amount;
    }

    public void earn(Long amount) {
        earn += amount;
    }

}
