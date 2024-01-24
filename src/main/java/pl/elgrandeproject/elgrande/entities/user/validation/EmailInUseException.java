package pl.elgrandeproject.elgrande.entities.user.validation;

public class EmailInUseException extends RuntimeException {

    public EmailInUseException(String message) {
        super(message);
    }
}
