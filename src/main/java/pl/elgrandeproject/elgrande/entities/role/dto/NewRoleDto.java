package pl.elgrandeproject.elgrande.entities.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.entities.role.validation.UnoccupiedRoleName;

@Getter
@Setter
@NoArgsConstructor
public class NewRoleDto {

    @UnoccupiedRoleName
    @NotBlank(message = "Nie moze być puste pole")
    String name;

    public NewRoleDto(String name) {
        this.name = name;
    }
}
