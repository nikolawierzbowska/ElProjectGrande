package pl.elgrandeproject.elgrande.entities.role.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.entities.role.validation.UnoccupiedRoleName;

@Getter
@Setter
@NoArgsConstructor
public class NewRoleDto {

    @UnoccupiedRoleName
    String name;

    public NewRoleDto(String name) {
        this.name = name;
    }
}
