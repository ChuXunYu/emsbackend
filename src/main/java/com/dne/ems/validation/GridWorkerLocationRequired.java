package com.dne.ems.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = GridWorkerLocationValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface GridWorkerLocationRequired {
    String message() default "当角色为网格员时，必须提供X和Y坐标";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 