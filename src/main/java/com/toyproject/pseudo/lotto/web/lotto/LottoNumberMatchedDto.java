package com.toyproject.pseudo.lotto.web.lotto;

import com.toyproject.pseudo.lotto.domain.lotto.LottoNumber;
import lombok.Data;

@Data
public class LottoNumberMatchedDto {
    Integer number;
    boolean isMatched;

    public LottoNumberMatchedDto(LottoNumber lottoNumber) {
        this.number = lottoNumber.getNumber();
        this.isMatched = lottoNumber.isMatched();
    }
}
