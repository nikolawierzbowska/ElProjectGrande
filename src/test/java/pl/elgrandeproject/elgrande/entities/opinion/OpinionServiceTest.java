package pl.elgrandeproject.elgrande.entities.opinion;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.elgrandeproject.elgrande.entities.course.Course;
import pl.elgrandeproject.elgrande.entities.course.CourseRepository;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.exception.OpinionNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;

import java.util.List;
import java.util.Optional;


class OpinionServiceTest {


    private final OpinionRepository opinionRepository = Mockito.mock(OpinionRepository.class);
    private final OpinionMapper opinionMapper = Mockito.mock(OpinionMapper.class);
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final OpinionService testOpinionService = new OpinionService(
            opinionRepository, opinionMapper, courseRepository,userRepository);

    //given:
    //when:
    //then:
    @Test
    void shouldReturnAllOpinionsByCourseId() {

        //given:
        Course course = new Course("course-test");
        Opinion opinion1 = new Opinion("desc1","name1");
        Opinion opinion2 = new Opinion("desc2","name2");

        List<Opinion> listOpinion = List.of(opinion1, opinion2);

        Mockito.when(opinionRepository.findOpinion(course.getId()))
                .thenReturn(listOpinion);

        OpinionDto opinionDto1 = new OpinionDto(opinion1.getId(), opinion1.getDescription(), opinion1.getUserName());
        OpinionDto opinionDto2 = new OpinionDto(opinion2.getId(), opinion2.getDescription(), opinion2.getUserName());

        List<OpinionDto> listDto = List.of(opinionDto1, opinionDto2);

        Mockito.when(opinionMapper.mapEntityToDto(listOpinion.get(0)))
                .thenReturn(listDto.get(0));
        Mockito.when(opinionMapper.mapEntityToDto(listOpinion.get(1)))
                .thenReturn(listDto.get(1));

        //when:
        List<OpinionDto> actual = testOpinionService.getAllOpinionsByCourseId(course.getId());

        //then:
        Assertions.assertThat(actual.size()).isEqualTo(2);
        Assertions.assertThat(actual).isEqualTo(listDto);

    }

    @Test
    void shouldReturnOpinionById() {
        //given:
        Course course = new Course("course-test");
        Opinion opinion = new Opinion("desc1","name1");
       OpinionDto opinionDto = new OpinionDto(opinion.getId(), opinion.getDescription(), opinion.getUserName());

        Mockito.when(opinionRepository.findOneById(course.getId(), opinion.getId()))
                .thenReturn(Optional.of(opinion));

        Mockito.when(opinionMapper.mapEntityToDto(opinion))
                .thenReturn(opinionDto);

        //when:
        OpinionDto actual = testOpinionService.getOpinionById(course.getId(), opinion.getId());

        //then:
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(opinionDto.id());

        Assertions.assertThat(actual.description()).isEqualTo(opinionDto.description());
        Assertions.assertThat(actual.userName()).isEqualTo(opinionDto.userName());
    }


    @Test
    void shouldThrowOpinionNotFoundException() {
        //given:
        Course course = new Course("course-test");
        Opinion opinion = new Opinion("desc1","name1");
       ;

        Mockito.when(opinionRepository.findOneById(course.getId(), opinion.getId()))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable =Assertions.catchThrowable(() ->
                testOpinionService.getOpinionById(course.getId(), opinion.getId()));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(OpinionNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("not found this opinion " + opinion.getId());


    }
}