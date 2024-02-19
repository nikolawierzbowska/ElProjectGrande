package pl.elgrandeproject.elgrande.entities.role.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.elgrandeproject.elgrande.entities.role.RoleRepository;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleFoundException;

public class ExistRoleNameValidator implements ConstraintValidator<UnoccupiedRoleName, String> {

    private final RoleRepository roleRepository;

    public ExistRoleNameValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public boolean isValid(String newNameRole, ConstraintValidatorContext constraintValidatorContext) {

        roleRepository.findByName(newNameRole.toUpperCase()).ifPresent(
                u -> {
                    throw new RoleFoundException(newNameRole + " istnieje już taka nazwa");
                }
        );
        return true;
    }
}
