package pl.elgrandeproject.elgrande.entities.course;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseFoundException;
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

    public CourseDto getCourseById(UUID courseId) {
        return courseRepository.findOneById(courseId)
                .map(entity -> courseMapper.mapEntityToDto(entity))
                .orElseThrow(() -> getCourseNotFoundException(courseId));
    }

    public CourseDto getCourseByName(String courseName) {

      if(courseRepository.findOneByName(courseName.toUpperCase())
                .map(entity -> courseMapper.mapEntityToDto(entity)).isEmpty()){
            return null;
        }
        return courseRepository.findOneByName(courseName.toUpperCase())
                .map(entity -> courseMapper.mapEntityToDto(entity)).get();
    }

    public CourseDto saveNewCourse(NewCourseDto newCourseDto) {
        String upperCaseName = newCourseDto.getName().toUpperCase();
        if(getCourseByName(upperCaseName) == null){
            newCourseDto.setName(upperCaseName);
            Course courseSaved = courseRepository.save(courseMapper.mapNewCourseDtoToEntity(newCourseDto));
            return courseMapper.mapEntityToDto(courseSaved);
        }
        throw new CourseFoundException("Exist this name course");

    }

    public void deleteCourseById(UUID courseId) {
        Course course = courseRepository.findOneById(courseId)
                .orElseThrow(() -> getCourseNotFoundException(courseId));

       courseRepository.delete(course);
    }

    public void updateCourseId(UUID courseId, NewCourseDto updateCourseDto) {
        Course course = courseRepository.findOneById(courseId)
                .orElseThrow(() -> getCourseNotFoundException(courseId));

        Course updatedCourse = courseMapper.mapNewCourseDtoToEntity(updateCourseDto);
        String upperCaseName = updatedCourse.getName().toUpperCase();
        if(getCourseByName(upperCaseName) == null){
            course.setName(upperCaseName);
            courseRepository.save(course);

        }else{
            throw new CourseFoundException("Exist this name course");
        }

    }

    public CourseNotFoundException getCourseNotFoundException(UUID courseId) {
        return new CourseNotFoundException("course with this id " + courseId + " not found");
    }

}
