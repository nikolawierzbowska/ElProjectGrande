package pl.elgrandeproject.elgrande.entities.opinion;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/courses")
public class OpinionController {

    private final OpinionService opinionService;

    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @GetMapping("/{courseId}/opinions")
    public List<OpinionDto> getAllOpinionsByCourseId(@PathVariable UUID courseId){
        return opinionService.getAllOpinionsByCourseId(courseId);
    }

    @GetMapping("/{courseId}/opinions/{opinionId}")
    public OpinionDto getOpinionById(@PathVariable UUID courseId, @PathVariable UUID opinionId) {
        return opinionService.getOpinionById(courseId, opinionId);

    }

    @PostMapping("/{courseId}/opinions")
    public OpinionDto createNewOpinion(@PathVariable UUID courseId, @Valid @RequestBody NewOpinionDto newOpinionDto){

        return opinionService.saveNewOpinion(courseId, newOpinionDto);
    }

    @DeleteMapping("/{courseId}/opinions/{opinionId}")
        public void deleteOpinion(@PathVariable UUID courseId, @PathVariable UUID opinionId) {
            opinionService.deleteOpinion(courseId, opinionId);

    }
}
