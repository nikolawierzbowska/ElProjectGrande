package pl.elgrandeproject.elgrande.entities.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;

public class UnoccupiedValidator implements ConstraintValidator<Unoccupied, String> {

    private final UserRepository userRepository;

    public UnoccupiedValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

        userRepository.findByEmail(email).ifPresent(
                u -> {
                    throw new EmailInUseException(email + " is already in use");
                }
        );
        return true;
    }
}
