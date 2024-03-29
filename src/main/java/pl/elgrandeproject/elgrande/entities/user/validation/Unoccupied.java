package pl.elgrandeproject.elgrande.entities.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UnoccupiedValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Unoccupied {
    String message() default "This email is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
