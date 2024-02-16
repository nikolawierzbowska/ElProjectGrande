package pl.elgrandeproject.elgrande.entities.opinion;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.course.Course;
import pl.elgrandeproject.elgrande.entities.course.CourseRepository;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.exception.OpinionNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.registration.Principal;

import java.util.List;
import java.util.UUID;

import static pl.elgrandeproject.elgrande.entities.course.CourseService.getCourseNotFoundException;
import static pl.elgrandeproject.elgrande.entities.user.UserService.getUserWithThisEmailNotFoundException;

@Service
public class OpinionService {
    private final OpinionRepository opinionRepository;
    private final OpinionMapper opinionMapper;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public OpinionService(OpinionRepository opinionRepository, OpinionMapper opinionMapper,
                          CourseRepository courseRepository, UserRepository userRepository) {
        this.opinionRepository = opinionRepository;
        this.opinionMapper = opinionMapper;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    List<OpinionDto> getAllOpinionsByCourseId(UUID courseId) {
        return opinionRepository.findOpinion(courseId).stream()
                .map(entity -> opinionMapper.mapEntityToDto(entity))
                .toList();
    }

    public OpinionDto getOpinionDtoById(UUID courseId, UUID opinionId) {
        return opinionRepository.findOneById(courseId, opinionId)
                .map(opinion -> opinionMapper.mapEntityToDto(opinion))
                .orElseThrow(() -> getOpinionNotFoundException(opinionId));
    }

    public OpinionDto saveNewOpinion(UUID courseId, NewOpinionDto newOpinionDto, Principal principal) {
        Course course = courseRepository.findOneById(courseId)
                .orElseThrow(() -> getCourseNotFoundException(courseId));

        Opinion opinion = opinionMapper.mapNewOpinionDtoToEntity(newOpinionDto);

        UserClass user = userRepository.findByEmail(principal.userDetails().getUsername())
                .orElseThrow(() -> getUserWithThisEmailNotFoundException(principal.userDetails().getUsername()));

        opinion.setUserName(user.getFirstName());
        course.addOpinion(opinion);
        Opinion savedOpinion = opinionRepository.save(opinion);
        return opinionMapper.mapEntityToDto(savedOpinion);
    }

    public void deleteOpinion(UUID courseId, UUID opinionId) {
        Opinion opinion = opinionRepository.findOneById(courseId, opinionId)
                .orElseThrow(() -> getOpinionNotFoundException(opinionId));
        opinionRepository.delete((opinion));
    }

    public OpinionNotFoundException getOpinionNotFoundException(UUID id) {
        return new OpinionNotFoundException("Nie znaleziono opini o takim id: " + id);
    }
}
