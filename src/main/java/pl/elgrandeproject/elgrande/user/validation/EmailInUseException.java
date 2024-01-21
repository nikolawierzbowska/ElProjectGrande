package pl.elgrandeproject.elgrande.user.validation;

public class EmailInUseException extends RuntimeException {

    public EmailInUseException(String message) {
        super(message);
    }
}
