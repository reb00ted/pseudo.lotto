package com.toyproject.pseudo.lotto.web.lotto;

import com.toyproject.pseudo.lotto.web.validator.LottoNumberValidation;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class LottoNumberDto {
    @LottoNumberValidation
    @Size(min = 0, max = 6)
    private List<Integer> numbers = new ArrayList<>();
}
