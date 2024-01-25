package pl.elgrandeproject.elgrande.entities.role.dto;

import java.util.UUID;

public record RoleDto(
        UUID id,
        String description
) {
}
