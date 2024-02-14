package pl.elgrandeproject.elgrande.entities.course;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseFoundException;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;
import pl.elgrandeproject.elgrande.entities.course.exception.LengthOfNewNameCourseException;

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

    public CourseDto getCourseDtoById(UUID courseId) {
        return courseRepository.findOneById(courseId)
                .map(entity -> courseMapper.mapEntityToDto(entity))
                .orElseThrow(() -> getCourseNotFoundException(courseId));
    }

    public Course getCourseById(UUID courseId) {
        return courseRepository.findOneById(courseId)
                .orElseThrow(() -> getCourseNotFoundException(courseId));
    }

    public CourseDto getCourseByName(String courseName) {
        return courseRepository.findOneByName(courseName.toUpperCase())
                .map(entity -> courseMapper.mapEntityToDto(entity))
                .orElse(null);
//                .orElseThrow(() -> getCourseNotFoundException(courseName));
    }


    public CourseDto saveNewCourse(NewCourseDto newCourseDto)  {
       int length = 50;
        if (newCourseDto.getName().length() > length) {
            throw lengthNameCourseException(newCourseDto, length);
        } else {
            String upperCaseName = newCourseDto.getName().toUpperCase();
            if (getCourseByName(upperCaseName) == null) {
                newCourseDto.setName(upperCaseName);
                Course courseSaved = courseRepository.save(courseMapper.mapNewCourseDtoToEntity(newCourseDto));
                return courseMapper.mapEntityToDto(courseSaved);
            } else {
                throw new CourseFoundException("Istnieje już taka nazwa kursu: " + newCourseDto.getName());
            }
        }
    }

    public void deleteCourseById(UUID courseId) {
        Course course = courseRepository.findOneById(courseId)
                .orElseThrow(() -> getCourseNotFoundException(courseId));
        courseRepository.delete(course);
    }



    public void updateCourse(UUID courseId, NewCourseDto updateCourseDto) {
        LengthOfNewNameCourseException lengthException = lengthNameCourseException(updateCourseDto, 50);
        if(lengthException ==null) {
            Course course = getCourseById(courseId);
            updateCourseDto.setName(updateCourseDto.getName().toUpperCase());
            Course updatedCourse = courseMapper.mapNewCourseDtoToEntity(updateCourseDto);

            if (getCourseByName(updatedCourse.getName()) == null) {
                course.setName(updatedCourse.getName());
                courseRepository.save(course);
            } else {
                throw new CourseFoundException("Istnieje już taka nazwa kursu: " + updateCourseDto.getName().toUpperCase());
            }
        }
    }

    public CourseNotFoundException getCourseNotFoundException(UUID courseId) {
        return new CourseNotFoundException("Kurs z takim ID " + courseId + " nie został znaleziony");
    }

    public CourseNotFoundException getCourseNotFoundException(String name) {
        return new CourseNotFoundException("Kurs z taką nazwą: " + name + " nie został znaleziony");
    }


    public LengthOfNewNameCourseException lengthNameCourseException(NewCourseDto newCourseDto, int length) {
        if (newCourseDto.getName().length() > length) {
            throw new LengthOfNewNameCourseException("Max. tytuł 50 znaków");
        }
        return null;
    }
}
