package com.juzi.validator;

import com.juzi.util.ValidationUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validation;

/**
 * @author: juzi
 * @date: 2019/4/7
 * @time: 19:50
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String>{

    private boolean required = false;


    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String mobile, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidationUtil.isMobile(mobile);
        }else {
            if(StringUtils.isEmpty(mobile)){
                return false;
            }else {
                return ValidationUtil.isMobile(mobile);
            }
        }
    }
}
