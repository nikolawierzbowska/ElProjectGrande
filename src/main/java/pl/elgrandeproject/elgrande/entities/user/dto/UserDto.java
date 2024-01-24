package pl.elgrandeproject.elgrande.user.dto;

import java.util.UUID;

public record UserDto(
         UUID id,
         String firstName,
         String lastName,
         String email,
         String password,
         String repeatedPassword
) {
}
