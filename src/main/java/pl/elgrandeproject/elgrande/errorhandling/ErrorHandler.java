package pl.elgrandeproject.elgrande.errorhandling;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseFoundException;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;
import pl.elgrandeproject.elgrande.entities.course.exception.LengthOfNewNameCourseException;
import pl.elgrandeproject.elgrande.entities.course.exception.LengthOfUpdateNameCourseException;
import pl.elgrandeproject.elgrande.entities.opinion.exception.OpinionNotFoundException;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleFoundException;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.exception.ForbiddenUserAccessException;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.validation.EmailInUseException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {


    public static RuntimeException ErrorResponse;

    @ExceptionHandler(EmailInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOccupiedEmail(EmailInUseException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(MethodArgumentNotValidException e) {
        String errMsg = e.getAllErrors().stream()
                .map(ex -> ex.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        return new ErrorResponse(errMsg);
    }

    @ExceptionHandler(ForbiddenUserAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse forbiddenUserAccess(ForbiddenUserAccessException e){
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFound(UserNotFoundException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse roleNotFound(RoleNotFoundException e){
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler(RoleFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse roleFound(RoleFoundException e){
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler(CourseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse courseNotFound(CourseNotFoundException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(CourseFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse nameOfCourseExist(CourseFoundException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(OpinionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse opinionNotFound(OpinionNotFoundException e){
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler(LengthOfUpdateNameCourseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse notCorrectLength(LengthOfUpdateNameCourseException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(LengthOfNewNameCourseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notCorrectLength(LengthOfNewNameCourseException e){
        return new ErrorResponse(e.getMessage());
    }



    public record ErrorResponse(String info) {

    }
}
