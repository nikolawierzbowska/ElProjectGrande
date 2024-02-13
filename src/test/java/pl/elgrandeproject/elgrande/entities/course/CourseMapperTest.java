package pl.elgrandeproject.elgrande.entities.course;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.elgrandeproject.elgrande.entities.course.dto.CourseDto;
import pl.elgrandeproject.elgrande.entities.course.dto.NewCourseDto;
import pl.elgrandeproject.elgrande.entities.opinion.Opinion;

import java.util.List;

class CourseMapperTest {

    private final CourseMapper testedCourseMapper = new CourseMapper();

    @Test
    void shouldMapNewCourseDtoToEntity() {
        //given:
        NewCourseDto newCourseDto = new NewCourseDto("test-name");

        //when:
        Course actual = testedCourseMapper.mapNewCourseDtoToEntity(newCourseDto);

        //then:
        Assertions.assertThat(actual.getName()).isEqualTo("test-name");
        Assertions.assertThat(actual.getId()).isNotNull();
        Assertions.assertThat(actual.getOpinions()).isEmpty();
    }

    @Test
    void shouldMapEntityToDto() {
        //given:
        Course course = new Course("test-name");
        Opinion opinion1 = new Opinion("test-description1", "test-userName1");
        Opinion opinion2 = new Opinion("test-description2", "test-userName2");
        List<Opinion> opinionLIst = List.of(opinion1, opinion2);
        course.setOpinions(opinionLIst);

        //when:
        CourseDto actual = testedCourseMapper.mapEntityToDto(course);

        //then:
        CourseDto expected = new CourseDto(course.getId(), course.getName(),
        List.of(
                new CourseDto.OpinionsToCourse(opinion1.getId(),"test-description1"),
                new CourseDto.OpinionsToCourse(opinion2.getId(), "test-description2")
        ));

        Assertions.assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(actual.opinions()).isNotNull();
        Assertions.assertThat(actual.opinions().size()).isEqualTo(2);
        Assertions.assertThat(actual.opinions().get(0)).isEqualTo(expected.opinions().get(0));
        Assertions.assertThat(actual.opinions().get(1)).isEqualTo(expected.opinions().get(1));
    }
}