package pl.elgrandeproject.elgrande.errorhandling;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.elgrandeproject.elgrande.entities.course.exception.CourseNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.validation.EmailInUseException;
import pl.elgrandeproject.elgrande.registration.exception.PasswordsNotMatchException;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleNotFoundException;

@RestControllerAdvice
public class ErrorHandler {


    public static RuntimeException ErrorResponse;

    @ExceptionHandler(EmailInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOccupiedEmail(EmailInUseException e) {
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

    @ExceptionHandler(PasswordsNotMatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse passwordsNotMatch(PasswordsNotMatchException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(CourseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse courseNotFound(CourseNotFoundException e){
        return new ErrorResponse(e.getMessage());
    }

    public record ErrorResponse(String info) {

    }
}
