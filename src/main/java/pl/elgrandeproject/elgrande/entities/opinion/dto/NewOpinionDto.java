package pl.elgrandeproject.elgrande.entities.opinion.dto;


import jakarta.validation.constraints.Size;

public record NewOpinionDto(
        @Size(max = 180, message = "The opinion can not be longer than 120 characters")
        String description,
        String userName
) {
}

