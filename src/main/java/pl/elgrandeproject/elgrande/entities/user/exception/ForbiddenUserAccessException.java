package pl.elgrandeproject.elgrande.entities.user.exception;

public class ForbiddenUserAccessException extends RuntimeException{

    public ForbiddenUserAccessException(String message) {
        super(message);
    }

}
