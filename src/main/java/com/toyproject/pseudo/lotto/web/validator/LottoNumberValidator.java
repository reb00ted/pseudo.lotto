package com.toyproject.pseudo.lotto.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class LottoNumberValidator implements ConstraintValidator<LottoNumberValidation, List<Integer>> {
    @Override
    public void initialize(LottoNumberValidation constraintAnnotation) {

    }

    @Override
    public boolean isValid(List<Integer> value, ConstraintValidatorContext context) {
        return value.stream().allMatch(num -> 1 <= num && num <= 45);
    }
}
