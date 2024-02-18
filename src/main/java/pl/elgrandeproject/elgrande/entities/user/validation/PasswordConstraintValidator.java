package pl.elgrandeproject.elgrande.entities.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.elgrandeproject.elgrande.entities.user.dto.NewUserDto;


public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, Object> {

     @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        NewUserDto newUserDto = (NewUserDto) object;
        return newUserDto.getPassword().equals(newUserDto.getRepeatedPassword());
    }
}
