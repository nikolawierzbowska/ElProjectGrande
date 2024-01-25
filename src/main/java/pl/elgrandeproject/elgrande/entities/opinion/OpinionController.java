package pl.elgrandeproject.elgrande.entities.opinion;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
public class OpinionController {

    private final OpinionService opinionService;

    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @GetMapping("/auth/opinions")
    public List<OpinionDto> getAllOpinions(){
        return opinionService.getOpinions();
    }

    @GetMapping("/user/{userId}/opinions/")
    public OpinionDto  getOpinionById(@PathVariable UUID userId ) {
        return opinionService.getOpinionById(userId);

    }

    @PostMapping("/user/opinions")
    public OpinionDto createNewOpinion(@Valid @RequestBody NewOpinionDto newOpinionDto){
        return opinionService.saveNewOpinion(newOpinionDto);
    }

//    @DeleteMapping("/user/{id}/opinions/{id}")
//        public void deleteOpinion(@PathVariable UUID id) {
//            opinionService.deleteOpinion(id);
//
//    }
}
