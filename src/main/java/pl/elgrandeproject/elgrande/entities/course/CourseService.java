package pl.elgrandeproject.elgrande.entities.course;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseFoundException;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;
import pl.elgrandeproject.elgrande.entities.course.exception.LengthOfNewNameCourseException;
import pl.elgrandeproject.elgrande.entities.course.exception.LengthOfUpdateNameCourseException;

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



    public CourseDto saveNewCourse(NewCourseDto newCourseDto) throws LengthOfNewNameCourseException {
       if(newCourseDto.getName().length()> 35) {
           throw new LengthOfNewNameCourseException("Max. tytuł 35 znaków");
       }else {
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

    public void updateCourseId(UUID courseId, NewCourseDto updateCourseDto) {
        LengthOfUpdateNameCourseException lengthException = lengthOfUpdateNameCourseException(updateCourseDto, 35);
        if(lengthException !=null){
            throw lengthException;
        }

        Course course = courseRepository.findOneById(courseId)
                .orElseThrow(() -> getCourseNotFoundException(courseId));
        updateCourseDto.setName(updateCourseDto.getName().toUpperCase());
        Course updatedCourse = courseMapper.mapNewCourseDtoToEntity(updateCourseDto);
        String upperCaseName = updatedCourse.getName();
        if(getCourseByName(upperCaseName) == null){
            course.setName(upperCaseName);
            courseRepository.save(course);

        }else{
            throw new CourseFoundException("Istnieje już taka nazwa kursu: " + updateCourseDto.getName().toUpperCase());
        }

    }

    public CourseNotFoundException getCourseNotFoundException(UUID courseId) {
        return new CourseNotFoundException("Kurs z takim ID " + courseId + " nie został znaleziony");
    }


    public LengthOfUpdateNameCourseException lengthOfUpdateNameCourseException(NewCourseDto newCourseDto, int length) {
        if( newCourseDto.getName().length() >length){
            return new LengthOfUpdateNameCourseException("Max. tytuł 35 znaków");
        }
        return null;
    }





}
