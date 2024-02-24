package pl.elgrandeproject.elgrande.entities.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRoleDto {


    @NotBlank(message = "Nie moze być puste pole")
    String name;

    public UpdateRoleDto(String name) {
        this.name = name;
    }
}
