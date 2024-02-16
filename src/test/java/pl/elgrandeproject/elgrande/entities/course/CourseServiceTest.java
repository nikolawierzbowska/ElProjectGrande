package pl.elgrandeproject.elgrande.entities.course;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseFoundException;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final CourseMapper courseMapper = Mockito.mock(CourseMapper.class);
    private final CourseService testCourseService = new CourseService(courseRepository, courseMapper);

    @Test
    void shouldReturnAllCoursesDto() {
        //given:
        Course course1 = new Course("name1");
        Course course2 = new Course("name2");
        List<Course> coursesList = List.of(course1, course2);

        Mockito.when(courseRepository.findAll()
        ).thenReturn(coursesList);

        CourseDto courseDto1 = new CourseDto(course1.getId(), course1.getName(), null);
        CourseDto courseDto2 = new CourseDto(course2.getId(), course2.getName(), null);

        List<CourseDto> expectedListDto = List.of(courseDto1, courseDto2);

        Mockito.when(courseMapper.mapEntityToDto(coursesList.get(0)))
                .thenReturn(expectedListDto.get(0));

        Mockito.when(courseMapper.mapEntityToDto(coursesList.get(1)))
                .thenReturn(expectedListDto.get(1));

        //when:
        List<CourseDto> actual = testCourseService.getAllCourses();

        //then:
        Assertions.assertThat(actual).isEqualTo(expectedListDto);
        Assertions.assertThat(actual.get(0).opinions()).isNull();
    }

    @Test
    void shouldReturnCourseDtoById() {
        //given:
        Course course = new Course("name1");
        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));

        CourseDto courseDto = new CourseDto(course.getId(), course.getName(), null);
        Mockito.when(courseMapper.mapEntityToDto(course))
                .thenReturn(courseDto);

        //when:
        CourseDto actual = testCourseService.getCourseDtoById(course.getId());

        //then:
        Assertions.assertThat(actual).isEqualTo(courseDto);
        Assertions.assertThat(actual.opinions()).isNull();
    }

    @Test
    void shouldThrowCourseDtoByIdNotFoundException() {
        //given:
        UUID id = UUID.randomUUID();
        Mockito.when(courseRepository.findOneById(id))
                .thenReturn(Optional.empty());

        //when:
        Throwable throwable = Assertions.catchThrowable(
                () -> testCourseService.getCourseDtoById(id));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(CourseNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Kurs z takim ID " + id +
                " nie został znaleziony");
    }

    @Test
    void shouldReturnCourseById() {
        //given:
        Course course = new Course("name1");
        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));

        //when:
        Course actual = testCourseService.getCourseById(course.getId());

        //then:
        Assertions.assertThat(actual).isEqualTo(course);
    }

    @Test
    void shouldThrowCourseByIdNotFoundException() {
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
        Course course = new Course("name1".toUpperCase());
        Mockito.when(courseRepository.findOneByName(course.getName()))
                .thenReturn(Optional.of(course));

        CourseDto courseDto = new CourseDto(course.getId(), course.getName(), null);
        Mockito.when(courseMapper.mapEntityToDto(course))
                .thenReturn(courseDto);

        //given:
        CourseDto actual = testCourseService.getCourseDtoByName(course.getName());

        //then:
        Assertions.assertThat(actual).isEqualTo(courseDto);
        Assertions.assertThat(actual.opinions()).isEqualTo(null);
    }

    @Test
    void shouldThrowCourseDtoByNameNotFoundException() {
        //given:
        String name = "nameNotExist";
        Mockito.when(courseRepository.findOneByName(name))
                .thenReturn(Optional.empty());

        //when:
        Throwable throwable = Assertions.catchThrowable(
                () -> testCourseService.getCourseDtoByName(name));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(CourseNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Kurs z taką nazwą: "
                + name + " nie został znaleziony");
    }

    @Test
    void shouldTrueIfThisNameCourseExist() {
        //give:
        Course course = new Course("name".toUpperCase());
        NewCourseDto newCourseDto = new NewCourseDto("name".toUpperCase());
        Mockito.when(courseRepository.findOneByName(newCourseDto.getName()))
                .thenReturn(Optional.of(course));
        //when:
        boolean actual = testCourseService.ifPresentCourseWithThisName(newCourseDto);

        //then:
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void shouldFalseIfThisNameCourseNotExist() {
        //give:
        Course course = new Course("name".toUpperCase());
        NewCourseDto newCourseDto = new NewCourseDto("nameNew".toUpperCase());
        Mockito.when(courseRepository.findOneByName(newCourseDto.getName()))
                .thenReturn(Optional.empty());
        //when:
        boolean actual = testCourseService.ifPresentCourseWithThisName(newCourseDto);

        //then:
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void shouldReturnSavedCourseDto() {
        //give:
        NewCourseDto newCourseDto = new NewCourseDto("test-nameCourse".toUpperCase());
        Course course = new Course(newCourseDto.getName());
        CourseDto courseDto = new CourseDto(course.getId(), course.getName(), null);

        Mockito.when(courseRepository.findOneByName(newCourseDto.getName()))
                .thenReturn(Optional.empty());

        Mockito.when(courseRepository.save(course))
                .thenReturn(course);
        Mockito.when(courseMapper.mapNewCourseDtoToEntity(newCourseDto))
                .thenReturn(course);

        Mockito.when(courseMapper.mapEntityToDto(course))
                .thenReturn(courseDto);

        //when:
        CourseDto actual = testCourseService.saveNewCourse(newCourseDto);

        //then:
        Assertions.assertThat(actual.id()).isEqualTo(courseDto.id());
        Assertions.assertThat(actual.name()).isEqualTo(courseDto.name());
    }

    @Test
    void shouldReturnCourseNotFoundException() {
        //given:
        UUID id = UUID.randomUUID();
        Mockito.when(courseRepository.findOneById(id))
                .thenReturn(Optional.empty());

        // when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testCourseService.deleteCourseById(id));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(CourseNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("Kurs z takim ID " + id + " nie został znaleziony");
    }

    @Captor
    private ArgumentCaptor<Course> courseArgumentCaptor;

    @Test
    void testDeleteCourseById() {
        //given:
        Course course = new Course("test-name");
        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));

        //when:
        testCourseService.deleteCourseById(course.getId());

        //then:
        Mockito.verify(courseRepository).delete(courseArgumentCaptor.capture());
    }

    @Test
    void shouldReturnUpdatedCourse() {
        //given:
        Course course = new Course("test-name".toUpperCase());
        NewCourseDto updateCourse = new NewCourseDto("update-name".toUpperCase());

        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));

        Mockito.when(courseMapper.mapNewCourseDtoToEntity(updateCourse))
                .thenReturn(course);

        course.setName(updateCourse.getName());
        Mockito.when(courseRepository.findOneByName(updateCourse.getName()))
                .thenReturn(Optional.empty());

        //when:
        testCourseService.updateCourse(course.getId(), updateCourse);
        //then:
        Mockito.verify(courseRepository).save(courseArgumentCaptor.capture());
        Assertions.assertThat(courseArgumentCaptor.getValue().getId()).isEqualTo(course.getId());
        Assertions.assertThat(courseArgumentCaptor.getValue().getName()).isEqualTo("update-name".toUpperCase());
    }

    @Test
    void shouldReturnCourseFoundException() {
        //given:
        Course course = new Course("test-name".toUpperCase());
        NewCourseDto updateCourse = new NewCourseDto("test-name".toUpperCase());
        CourseDto courseDto = new CourseDto(course.getId(), course.getName(), null);

        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));

        Mockito.when(courseMapper.mapNewCourseDtoToEntity(updateCourse))
                .thenReturn(course);

        Mockito.when(courseMapper.mapEntityToDto(course))
                .thenReturn(courseDto);

        Mockito.when(courseRepository.findOneByName(updateCourse.getName()))
                .thenReturn(Optional.of(course));

        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testCourseService.updateCourse(course.getId(), updateCourse));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(CourseFoundException.class);
        Assertions.assertThat(throwable)
                .hasMessage(("Istnieje już taka nazwa kursu: " + updateCourse.getName()));
    }
}
