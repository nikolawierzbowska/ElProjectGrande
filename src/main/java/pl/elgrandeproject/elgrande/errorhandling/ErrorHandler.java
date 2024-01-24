package pl.elgrandeproject.elgrande.errorhandling;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.validation.EmailInUseException;
import pl.elgrandeproject.elgrande.registration.exception.PasswordsNotMatchException;

@RestControllerAdvice
public class ErrorHandler {

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

    @ExceptionHandler(PasswordsNotMatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse passwordsNotMatch(PasswordsNotMatchException e){
        return new ErrorResponse(e.getMessage());
    }

    public record ErrorResponse(String info) {

    }
}
