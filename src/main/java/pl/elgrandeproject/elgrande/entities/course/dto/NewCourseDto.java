package pl.elgrandeproject.elgrande.entities.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.entities.course.validation.UnoccupiedCourseName;

@Getter
@Setter
@NoArgsConstructor
public class NewCourseDto {

        @NotBlank(message ="Nie może być puste pole")
        @UnoccupiedCourseName
        @Size(max = 50, message = "Nazwa kursu nie może być dłuższa niż 50 znaków.")
        String name;

        public NewCourseDto(String name) {
                this.name = name;
        }
}
