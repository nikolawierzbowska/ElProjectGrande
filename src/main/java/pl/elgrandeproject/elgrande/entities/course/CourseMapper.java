package pl.elgrandeproject.elgrande.entities.course;

import org.springframework.stereotype.Component;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;

@Component
public class CourseMapper {

    public Course mapNewCourseDtoToEntity(NewCourseDto newCourseDto){
        return new Course(newCourseDto.name());
    }

    public CourseDto mapEntityToDto( Course course) {
        return new CourseDto(course.getId() ,course.getName());

    }

}
