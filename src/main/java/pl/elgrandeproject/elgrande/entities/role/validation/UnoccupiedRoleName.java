package pl.elgrandeproject.elgrande.entities.role.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistRoleNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UnoccupiedRoleName {
    String message() default "Istnieje już taka nazwa";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
