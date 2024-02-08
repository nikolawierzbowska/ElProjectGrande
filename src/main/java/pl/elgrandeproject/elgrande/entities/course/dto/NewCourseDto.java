package pl.elgrandeproject.elgrande.entities.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCourseDto {

        @NotBlank(message = "The field can not be empty.")
        @Size(max = 35, message = "The name can not be longer than 35 characters")
        String name;
}
