package pl.elgrandeproject.elgrande.entities.opinion;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;

import pl.elgrandeproject.elgrande.registration.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class OpinionController {

    private final OpinionService opinionService;

    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @GetMapping("courses/{courseId}/opinions")
    public List<OpinionDto> getAllOpinionsByCourseId(@PathVariable UUID courseId){
        return opinionService.getAllOpinionsByCourseId(courseId);
    }

    @GetMapping("courses/{courseId}/opinions/{opinionId}")
    public OpinionDto getOpinionById(@PathVariable UUID courseId, @PathVariable UUID opinionId) {
        return opinionService.getOpinionDtoById(courseId, opinionId);
    }

    @PostMapping("courses/{courseId}/opinions")
    public OpinionDto createNewOpinion(@PathVariable UUID courseId, @Valid @RequestBody NewOpinionDto newOpinionDto, Principal principal){
        return opinionService.saveNewOpinion(courseId, newOpinionDto, principal);
    }

    @DeleteMapping("admin/courses/{courseId}/opinions/{opinionId}")
        public void deleteOpinion(@PathVariable UUID courseId, @PathVariable UUID opinionId) {
            opinionService.deleteOpinion(courseId, opinionId);

    }
}
