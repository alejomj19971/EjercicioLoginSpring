package com.tienda.login.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExistsByUserNameValidation.class )
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public  @interface ExistsByUserName {
    String message() default "El campo username ya existe en la base de datos, escoja otro username";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
