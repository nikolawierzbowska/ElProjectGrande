package pl.elgrandeproject.elgrande.entities.opinion.dto;


import java.util.UUID;

public record OpinionDto(
        UUID id,
        String description,
        UserAddOpinion user

) {
    public record UserAddOpinion (
        UUID id,
        String firstName,
        String lastName,
        String email,
        String password,
        String repeatedPassword
) {

}

}

