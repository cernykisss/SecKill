package com.obito.seckill.vo;

import com.obito.seckill.util.ValidationUtil;
import com.obito.seckill.validator.IsMobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidationUtil.isMobile(value);
        } else {
            if (value.isEmpty()) {
                return true;
            } else {
                return ValidationUtil.isMobile(value);
            }
        }
    }
}
