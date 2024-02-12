package pl.elgrandeproject.elgrande.entities.opinion;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.course.Course;
import pl.elgrandeproject.elgrande.entities.course.CourseRepository;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.exception.OpinionNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class OpinionService {
    private final OpinionRepository opinionRepository;
    private final OpinionMapper opinionMapper;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public OpinionService(OpinionRepository opinionRepository, OpinionMapper opinionMapper, CourseRepository courseRepository, UserRepository userRepository) {
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

    public OpinionDto getOpinionById(UUID courseId, UUID opinionId) {
        return opinionRepository.findOneById(courseId, opinionId)
                .map(opinion -> opinionMapper.mapEntityToDto(opinion))
                .orElseThrow(() -> getOpinionNotFoundException(opinionId));
    }


    public OpinionDto saveNewOpinion(UUID courseId, NewOpinionDto newOpinionDto) {
        Course course = courseRepository.findOneById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Kurs z takim ID: " + courseId + "  nie został znaleziony"));

        Opinion opinion = opinionMapper.mapNewOpinionDtoToEntity(newOpinionDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        UserClass user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("not found this user"));

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
        return new OpinionNotFoundException("not found this opinion " + id);
    }
}
