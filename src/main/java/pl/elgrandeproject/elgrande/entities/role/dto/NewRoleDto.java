package pl.elgrandeproject.elgrande.entities.role.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewRoleDto {
    String name;

    public NewRoleDto(String name) {
        this.name = name;
    }
}
