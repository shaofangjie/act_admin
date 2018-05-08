package com.act.admin.commonutil.validation;

/**
 * Created by shaofangjie on 2017/5/15.
 */

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=EmptyOrPatternValidator.class)
public @interface EmptyOrPattern {

    String message() default"格式错误";
    String regexp() ;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
