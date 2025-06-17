package com.dne.ems.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "无效的密码：密码必须至少8个字符长，包含至少一个大写字母，一个小写字母，一个数字和一个特殊字符。";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 