package pl.elgrandeproject.elgrande.entities.course;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> courseMapper.mapEntityToDto(course))
                .toList();
    }

    public CourseDto getCourseById(UUID id) {
        return courseRepository.findOneById(id)
                .map(entity -> courseMapper.mapEntityToDto(entity))
                .orElseThrow(() -> getCourseNotFoundException(id));
    }

    public CourseDto saveNewCourse(NewCourseDto newCourseDto) {
        Course courseSaved = courseRepository.save(courseMapper.mapNewCourseDtoToEntity(newCourseDto));
        return courseMapper.mapEntityToDto(courseSaved);
    }

    public void deleteCourseById(UUID id) {
        Course course = courseRepository.findOneById(id)
                .orElseThrow(() -> getCourseNotFoundException(id));

       courseRepository.delete(course);

    }

    public CourseNotFoundException getCourseNotFoundException(UUID id) {
        return new CourseNotFoundException("course with this id " + id + " not found");
    }
}
