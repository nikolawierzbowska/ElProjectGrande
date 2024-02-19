package pl.elgrandeproject.elgrande.entities.course.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UnoccupiedCourseName {
    String message() default "Istnieje już taka nazwa";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
