package pl.elgrandeproject.elgrande.entities.opinion;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import pl.elgrandeproject.elgrande.entities.course.Course;
import pl.elgrandeproject.elgrande.entities.course.CourseRepository;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.exception.OpinionNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;
import pl.elgrandeproject.elgrande.registration.Principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class OpinionServiceTest {
    private final OpinionRepository opinionRepository = Mockito.mock(OpinionRepository.class);
    private final OpinionMapper opinionMapper = Mockito.mock(OpinionMapper.class);
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final OpinionService testOpinionService = new OpinionService(
            opinionRepository, opinionMapper, courseRepository, userRepository);

    @Test
    void shouldReturnAllOpinionsDtoByCourseId() {
        //given:
        Course course = new Course("course-test");
        Opinion opinion1 = new Opinion("desc1", "name1");
        Opinion opinion2 = new Opinion("desc2", "name2");

        List<Opinion> listOpinion = List.of(opinion1, opinion2);

        Mockito.when(opinionRepository.findOpinion(course.getId()))
                .thenReturn(listOpinion);

        OpinionDto opinionDto1 = new OpinionDto(opinion1.getId(), opinion1.getDescription(), opinion1.getUserName());
        OpinionDto opinionDto2 = new OpinionDto(opinion2.getId(), opinion2.getDescription(), opinion2.getUserName());

        List<OpinionDto> listDto = List.of(opinionDto2, opinionDto1);

        Mockito.when(opinionMapper.mapEntityToDto(listOpinion.get(0)))
                .thenReturn(listDto.get(1));
        Mockito.when(opinionMapper.mapEntityToDto(listOpinion.get(1)))
                .thenReturn(listDto.get(0));

        //when:
        List<OpinionDto> actual = testOpinionService.getAllOpinionsByCourseId(course.getId());


        //then:
        Assertions.assertThat(actual.size()).isEqualTo(2);
        Assertions.assertThat(actual).isEqualTo(listDto);
    }

    @Test
    void shouldReturnOpinionDtoById() {
        //given:
        Course course = new Course("course-test");
        Opinion opinion = new Opinion("desc1", "name1");
        OpinionDto opinionDto = new OpinionDto(opinion.getId(), opinion.getDescription(), opinion.getUserName());

        Mockito.when(opinionRepository.findOneById(course.getId(), opinion.getId()))
                .thenReturn(Optional.of(opinion));

        Mockito.when(opinionMapper.mapEntityToDto(opinion))
                .thenReturn(opinionDto);

        //when:
        OpinionDto actual = testOpinionService.getOpinionDtoById(course.getId(), opinion.getId());

        //then:
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(opinionDto.id());
        Assertions.assertThat(actual.description()).isEqualTo(opinionDto.description());
        Assertions.assertThat(actual.userName()).isEqualTo(opinionDto.userName());
    }

    @Test
    void shouldThrowOpinionDtoByIdNotFoundException() {
        //given:
        Course course = new Course("course-test");
        Opinion opinion = new Opinion("desc1", "name1");

        Mockito.when(opinionRepository.findOneById(course.getId(), opinion.getId()))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testOpinionService.getOpinionDtoById(course.getId(), opinion.getId()));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(OpinionNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Nie znaleziono opini o takim id: " + opinion.getId());
    }

    @Test
    void shouldThrowCourseNotFoundException() {
        //given:
        Course course = new Course("course-test");
        NewOpinionDto newOpinionDto = new NewOpinionDto("desc1", "name1");
        Principal principal = new Principal();

        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.empty());

        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testOpinionService.saveNewOpinion(course.getId(), newOpinionDto, principal));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(CourseNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("Kurs z takim ID " + course.getId() + " nie został znaleziony");
    }

    @Test
    void shouldThrowUserNotFoundException() {
        //given:
        Course course = new Course("course-test");
        NewOpinionDto newOpinionDto = new NewOpinionDto("desc1", "name1");
        Opinion opinion = new Opinion(newOpinionDto.description(), newOpinionDto.userName());
        Principal principal = new Principal();
        UserDetails userDetails = new User("email@vp.pl", "password", new ArrayList<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));

        Mockito.when(opinionMapper.mapNewOpinionDtoToEntity(newOpinionDto))
                .thenReturn(opinion);

        SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(userRepository.findByEmail("email"))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testOpinionService.saveNewOpinion(course.getId(), newOpinionDto, principal));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        Assertions.assertThat(throwable).hasMessage("User z tym email email@vp.pl nie istnieje");
    }

    @Test
    void shouldReturnSaveOpinion() {
        //given:
        UserClass userClass = new UserClass("name", "lastname", "email", "password",
                "rPassword");
        Course course = new Course("course-test");
        NewOpinionDto newOpinionDto = new NewOpinionDto("desc1", "name1");
        Opinion opinion = new Opinion(newOpinionDto.description(), newOpinionDto.userName());
        OpinionDto opinionDto = new OpinionDto(opinion.getId(), opinion.getDescription(), opinion.getUserName());
        Principal principal = new Principal();

        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));

        Mockito.when(opinionMapper.mapNewOpinionDtoToEntity(newOpinionDto))
                .thenReturn(opinion);

        UserDetails userDetails = new User("email", "password", new ArrayList<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(userRepository.findByEmail("email"))
                .thenReturn(Optional.of(userClass));

        opinion.setUserName(userClass.getFirstName());
        course.addOpinion(opinion);

        Mockito.when(opinionRepository.save(opinion))
                .thenReturn(opinion);
        Mockito.when(opinionMapper.mapEntityToDto(opinion))
                .thenReturn(opinionDto);

        //when:
        OpinionDto actual = testOpinionService.saveNewOpinion(course.getId(), newOpinionDto, principal);

        //then:
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.id()).isEqualTo(opinionDto.id());
        Assertions.assertThat(actual.description()).isEqualTo(opinionDto.description());
        Assertions.assertThat(actual.userName()).isEqualTo(opinionDto.userName());
    }

    @Test
    void shouldThrowOpinionNotFoundExceptionWhenDelete() {
        //given:
        Course course = new Course("course-test");
        Opinion opinion = new Opinion("desc1", "name1");
        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));

        Mockito.when(opinionRepository.findOneById(course.getId(), opinion.getId()))
                .thenReturn(Optional.empty());
        //when:
        Throwable throwable = Assertions.catchThrowable(() ->
                testOpinionService.deleteOpinion(course.getId(), opinion.getId()));
        //then:
        Assertions.assertThat(throwable).isInstanceOf(OpinionNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Nie znaleziono opini o takim id: " + opinion.getId());
    }

    @Test
    void testDeleteOpinion() {
        //given:
        Course course = new Course("course-test");
        Opinion opinion = new Opinion("desc1", "name1");
        Mockito.when(courseRepository.findOneById(course.getId()))
                .thenReturn(Optional.of(course));
        Mockito.when(opinionRepository.findOneById(course.getId(), opinion.getId()))
                .thenReturn(Optional.of(opinion));
        course.removeOpinion(opinion);
        //when:

        testOpinionService.deleteOpinion(course.getId(), opinion.getId());

        //then:
        Mockito.verify(opinionRepository).deleteById(opinion.getId());
    }
}