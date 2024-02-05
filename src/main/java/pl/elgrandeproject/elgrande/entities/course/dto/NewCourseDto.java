package pl.elgrandeproject.elgrande.entities.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewCourseDto(

        @NotBlank(message = "The field can not be empty.")
        @Size(max = 35, message = "The name can not be longer than 35 characters")
        String name
) {
}
