package pl.elgrandeproject.elgrande.entities.user.dto;

import java.util.List;
import java.util.UUID;

public record UserDto(
         UUID id,
         String firstName,
         String lastName,
         String email,
         String password,
         String repeatedPassword,
         List<userRole> userRoles
) {
    public  record userRole(
            UUID id,
            String name
    ){

    }
}
