package pl.elgrandeproject.elgrande.entities.opinion;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.elgrandeproject.elgrande.entities.course.Course;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;

class OpinionMapperTest {

    private final OpinionMapper testOpinionMapper = new OpinionMapper();


    @Test
    void shouldMapNewOpinionDtoToEntity() {
        //given:
        NewOpinionDto newOpinionDto = new NewOpinionDto("test-description", "test-userName");
        Course course = new Course("test-nameCourse");

        //when:
        Opinion actual = testOpinionMapper.mapNewOpinionDtoToEntity(newOpinionDto);
        course.addOpinion(actual);

        //then:

        Assertions.assertThat(actual.getDescription()).isEqualTo("test-description");
        Assertions.assertThat(actual.getUserName()).isEqualTo("test-userName");
        Assertions.assertThat(actual.getDescription().length()).isLessThan(180);
        Assertions.assertThat(actual.getCourses()).isNotNull();
        Assertions.assertThat(actual.getCourses().getName()).isEqualTo("test-nameCourse");
        Assertions.assertThat(actual.getId()).isNotNull();
    }

    @Test
    void shouldMapEntityToDto() {
        //given:
        Course course = new Course("test-course");
        Opinion opinion = new Opinion("test-description", "test-userName");

        //when:
        OpinionDto actual = testOpinionMapper.mapEntityToDto(opinion);
        //then:


        Assertions.assertThat(actual.id()).isEqualTo(opinion.getId());
        Assertions.assertThat(actual.description()).isEqualTo("test-description");
        Assertions.assertThat(actual.userName()).isEqualTo("test-userName");
    }
}