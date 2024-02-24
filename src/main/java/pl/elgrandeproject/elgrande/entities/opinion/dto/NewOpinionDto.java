package pl.elgrandeproject.elgrande.entities.opinion.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewOpinionDto(
        @NotBlank(message = "Pole nie może być puste")
        @Size(max = 180, message = "Opinia może zawierać max. 180 znaków")
        String description,
        String userName
) {
}

