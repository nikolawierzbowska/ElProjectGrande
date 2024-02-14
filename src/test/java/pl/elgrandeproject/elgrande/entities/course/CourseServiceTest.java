package pl.elgrandeproject.elgrande.entities.course;


import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
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
import pl.elgrandeproject.elgrande.entities.course.exception.LengthOfNewNameCourseException;

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
        CourseDto actual = testCourseService.getCourseDtoById(course.getId());

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
                () -> testCourseService.getCourseDtoById(id));

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
//        Course course = Instancio.create(Course.class);
        Mockito.when(courseRepository.findOneByName("test-name"))
                .thenReturn(null);

        //when:
        CourseDto actual = testCourseService.getCourseByName("test-name");


        //then:
        Assertions.assertThat(actual).isNull();
    }

    @Test
    void shouldReturnLengthNameCourseException() {
        //given:
        NewCourseDto newCourseDto = new NewCourseDto("nameCourseContainsToMuchCharactersThanIsValidInThisClassCourse");

        //when:
        Throwable throwable = Assertions.catchThrowable(
                () -> testCourseService.saveNewCourse(newCourseDto));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(LengthOfNewNameCourseException.class);
        Assertions.assertThat(throwable).hasMessage("Max. tytuł 50 znaków");
    }

    @Test
    void shouldReturnSavedCourseDto() {
        //give:
        NewCourseDto newCourseDto = new NewCourseDto("test-nameCourse");
        Course course = new Course(newCourseDto.getName());
        CourseDto courseDto = new CourseDto(course.getId(), course.getName(), null);

        Mockito.when(courseMapper.mapNewCourseDtoToEntity(newCourseDto))
                        .thenReturn(course);
        Mockito.when(courseMapper.mapEntityToDto(course))
                        .thenReturn(courseDto);

        Mockito.when(courseRepository.save(course))
                .thenReturn(course);

        //when:
        CourseDto actual = testCourseService.saveNewCourse(newCourseDto);

        //then:
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(courseDto.id());
        Assertions.assertThat(actual.name()).isEqualTo(courseDto.name());
    }

    //TODO TEST PONIŻEJ
//    @Test
//    void shouldReturnCourseFoundExceptionIfNameCourseExist() {
//        //given:
//        Course course = new Course("MATURA Z MATEMATYKI PODSTAWOWEJ");
//
//        NewCourseDto newCourseDto = new NewCourseDto("MATURA Z MATEMATYKI PODSTAWOWEJ");
//
//
//        Mockito.when(courseRepository.findOneByName(newCourseDto.getName()))
//                .thenReturn(Optional.of(course));
//
//
//
//        //when:
//        Throwable throwable = Assertions.catchThrowable(() ->
//                testCourseService.saveNewCourse(newCourseDto));
//        //then:
//        Assertions.assertThat(throwable).isInstanceOf(CourseFoundException.class);
//
//
//    }


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

        //todo poniższa linijka jak usunełma to jak spr. czy kursu nie ma?
        Assertions.assertThat(courseRepository.findOneById(course.getId())).isNull();
    }


    @Test
    void shouldReturnLengthOfNewNameCourseException() {
        //given:
        Course course = new Course("test-name");
        NewCourseDto updateCourse =  new NewCourseDto("nameCourseContainsToMuchCharactersThanIsValidInThisClassCourse");

        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testCourseService.updateCourse(course.getId(),updateCourse));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(LengthOfNewNameCourseException.class);
        Assertions.assertThat(throwable).hasMessage(("Max. tytuł 50 znaków"));
    }



    @Test
    void shouldReturnUpdatedCourse() {

        //given:
        Course course = new Course("test-name");
        NewCourseDto updateCourse =  new NewCourseDto("update-name");

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
        Assertions.assertThat(courseArgumentCaptor.getValue().getName()).isEqualTo("update-name");

    }

    @Test
    void shouldReturnCourseFoundException() {
        //TODO czy jest okey odnośnie metody z servicu?
        //given:
        Course course = new Course("test-name".toUpperCase());
        NewCourseDto updateCourse =  new NewCourseDto("test-name".toUpperCase());
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


