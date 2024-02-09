package pl.elgrandeproject.elgrande.entities.course.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCourseDto {

        @NotNull
//        @NotBlank(message = "The field can not be empty.")
        @Size(max = 35, message = "The name can not be longer than 35 characters")
        String name;
}
