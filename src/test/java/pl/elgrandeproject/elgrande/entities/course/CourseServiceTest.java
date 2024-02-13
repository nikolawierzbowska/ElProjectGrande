package pl.elgrandeproject.elgrande.entities.course;


import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class CourseServiceTest {

    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final CourseMapper courseMapper = Mockito.mock(CourseMapper.class);
    private final CourseService testCourseService = new CourseService(courseRepository, courseMapper);

    @Test
    void shouldReturnAllCoursesDto() {
        //given:
        Course course1 = Instancio.of(Course.class).create();
        Course course2 = Instancio.of(Course.class).create();
        List<Course> coursesList = List.of(course1, course2);

        Mockito.when(courseRepository.findAll()
        ).thenReturn(coursesList);

        CourseDto courseDto1 = Instancio.of(CourseDto.class).create();
        CourseDto courseDto2 = Instancio.of(CourseDto.class).create();

        List<CourseDto> expectedListDto = List.of(courseDto1, courseDto2);

        Mockito.when(courseMapper.mapEntityToDto(coursesList.get(0)))
                .thenReturn(expectedListDto.get(0));

        Mockito.when(courseMapper.mapEntityToDto(coursesList.get(1)))
                .thenReturn(expectedListDto.get(1));

        //when:
        List<CourseDto> actual = testCourseService.getAllCourses();

        //then:
        Assertions.assertThat(actual).isEqualTo(expectedListDto);
    }

    @Test
    void shouldReturnCourseDtoById() {
        //given:
        Course course = Instancio.create(Course.class);
        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));

        CourseDto courseDto = Instancio.create(CourseDto.class);
        Mockito.when(courseMapper.mapEntityToDto(course))
                .thenReturn(courseDto);
        //when:
        CourseDto actual = testCourseService.getCourseById(course.getId());

        //then:
        Assertions.assertThat(actual).isEqualTo(courseDto);
    }

    @Test
    void shouldThrowCourseNotFoundException() {
        //given:
        UUID id = UUID.randomUUID();
        Mockito.when(courseRepository.findOneById(id))
                .thenReturn(Optional.empty());

        //when:
        Throwable throwable = Assertions.catchThrowable(
                () -> testCourseService.getCourseById(id));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(CourseNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Kurs z takim ID " + id +
                " nie został znaleziony");
    }

    @Test
    void shouldReturnCourseDtoByName() {
        //when:
        Course course = Instancio.create(Course.class);
        Mockito.when(courseRepository.findOneByName(course.getName()))
                .thenReturn(Optional.of(course));

        CourseDto courseDto = Instancio.create(CourseDto.class);
        Mockito.when(courseMapper.mapEntityToDto(course))
                .thenReturn(courseDto);

        //given:
        CourseDto actual = testCourseService.getCourseByName(course.getName());

        //then:
        Assertions.assertThat(actual).isEqualTo(courseDto);
    }

    @Test
    void shouldThrowCourseNotFoundExceptionByName() {
        //given:
        Course course = Instancio.create(Course.class);
        Mockito.when(courseRepository.findOneByName(course.getName()))
                .thenReturn(Optional.of(course));

        //when:
        Throwable throwable = Assertions.catchThrowable(() -> testCourseService.getCourseByName(course.getName()));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(CourseNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Kurs z taką nazwą: " + course.getName() + " nie został znaleziony");

    }
}


