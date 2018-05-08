package com.act.admin.commonutil.validation;

/**
 * Created by shaofangjie on 2017/5/15.
 */

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmptyOrPatternValidator implements ConstraintValidator<EmptyOrPattern, String> {

    private Pattern pattern ;

    @Override
    public void initialize(EmptyOrPattern emptyOrPattern) {
        pattern = Pattern.compile(emptyOrPattern.regexp());

    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext arg1) {
        return null == value || value.isEmpty() || pattern.matcher(value).matches();
    }

}