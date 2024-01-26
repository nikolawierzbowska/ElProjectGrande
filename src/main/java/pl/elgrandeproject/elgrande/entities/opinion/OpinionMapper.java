package pl.elgrandeproject.elgrande.entities.opinion;

import org.springframework.stereotype.Component;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;

@Component
public class OpinionMapper {

    public Opinion mapNewOpinionDtoToEntity(NewOpinionDto newOpinionDto) {
        return new Opinion(newOpinionDto.description());
    }

    public OpinionDto mapEntityToDto(Opinion opinion){
        return new OpinionDto(opinion.getId(), opinion.getDescription());

    }

}
