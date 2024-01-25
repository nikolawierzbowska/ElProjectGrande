package pl.elgrandeproject.elgrande.entities.opinion;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.opinion.dto.NewOpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.dto.OpinionDto;
import pl.elgrandeproject.elgrande.entities.opinion.exception.OpinionNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class OpinionService {

    private final OpinionRepository opinionRepository;
    private final OpinionMapper opinionMapper;

    public OpinionService(OpinionRepository opinionRepository, OpinionMapper opinionMapper) {
        this.opinionRepository = opinionRepository;
        this.opinionMapper = opinionMapper;
    }

    List<OpinionDto> getOpinions() {
        return opinionRepository.findAllBy().stream()
                .map(entity -> opinionMapper.mapEntityToDto(entity))
                .toList();
    }

    public OpinionDto getOpinionById(UUID id) {
        return opinionRepository.findOneById(id)
                .map(opinion -> opinionMapper.mapEntityToDto(opinion))
                .orElseThrow(() -> getOpinionNotFoundException(id));
    }


    public OpinionDto saveNewOpinion(NewOpinionDto newOpinionDto) {
        Opinion savedOpinion = opinionRepository.save(opinionMapper.mapNewOpinionDtoToEntity(newOpinionDto));
        return opinionMapper.mapEntityToDto(savedOpinion);
    }


    public void deleteOpinion(UUID id) {
        Opinion opinion = opinionRepository.findOneById(id)
                .orElseThrow(() -> getOpinionNotFoundException(id));

        if (opinion != null) {
            opinionRepository.delete((opinion));
        }
    }

    public OpinionNotFoundException getOpinionNotFoundException(UUID id) {
        return new OpinionNotFoundException("not found this opinion " + id);
    }
}
