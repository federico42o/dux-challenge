package org.f420.duxchallenge.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación personalizada que permite valores nulos,
 * pero si se proporciona un valor, este no debe estar vacío ni contener solo espacios.
 * <p>
 * </pre>
 */
@Constraint(validatedBy = ValueValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
    String message() default "El campo no puede estar vacío";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
