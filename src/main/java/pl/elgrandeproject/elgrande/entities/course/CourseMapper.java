package pl.elgrandeproject.elgrande.entities.course;

import org.springframework.stereotype.Component;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;

import java.util.List;

@Component
public class CourseMapper {

    public Course mapNewCourseDtoToEntity(NewCourseDto newCourseDto){
        return new Course(newCourseDto.getName());
    }

    public CourseDto mapEntityToDto( Course course) {
        return new CourseDto(course.getId() ,course.getName(), mapOpinions(course));

    }

    public List<CourseDto.OpinionsToCourse> mapOpinions(Course course){
        return course.getOpinions().stream()
                .map(op -> new CourseDto.OpinionsToCourse(op.getId(), op.getDescription()))
                .toList();
    }

}
