package pl.elgrandeproject.elgrande.entities.course.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewCourseDto {

        @NotNull
        @Size(max = 50, message = "The name can not be longer than 35 characters")
        String name;

        public NewCourseDto(@NotNull String name) {
                this.name = name;
        }
}
