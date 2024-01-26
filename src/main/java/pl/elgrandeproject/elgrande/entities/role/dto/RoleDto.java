package pl.elgrandeproject.elgrande.entities.role.dto;

import java.util.List;
import java.util.UUID;

public record RoleDto(
        UUID id,
        String name,
        List<UserNameId> users
) {

    public  record UserNameId(
            UUID id,
            String email
    ){
    }
}
