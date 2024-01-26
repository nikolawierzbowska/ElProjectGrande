package pl.elgrandeproject.elgrande.entities.course.dto;

import java.util.UUID;

public record CourseDto(
        UUID id,
        String name
//        List<OpinionsToCourse> opinions
) {

//    public record OpinionsToCourse (
//            UUID id,
//            String description
//
//    ){


}
