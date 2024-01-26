package pl.elgrandeproject.elgrande.entities.opinion.dto;


import jakarta.validation.constraints.Size;

public record NewOpinionDto(
        @Size(max = 80, message = "The opinion can not be longer than 80 characters")
        String description
) {
}

