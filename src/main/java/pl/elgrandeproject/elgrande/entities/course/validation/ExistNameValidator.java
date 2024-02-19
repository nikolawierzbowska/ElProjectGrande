package pl.elgrandeproject.elgrande.entities.course.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.elgrandeproject.elgrande.entities.course.CourseRepository;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseFoundException;

public class ExistNameValidator implements ConstraintValidator<UnoccupiedCourseName, String> {

    private final CourseRepository courseRepository;

    public ExistNameValidator( CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    @Override
    public boolean isValid(String newNameCourse, ConstraintValidatorContext constraintValidatorContext) {

        courseRepository.findOneByName(newNameCourse.toUpperCase()).ifPresent(
                u -> {
                    throw new CourseFoundException(newNameCourse + " istnieje już taka nazwa");
                }
        );
        return true;
    }
}
